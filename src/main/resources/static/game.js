const totalQuestions = 10; // 合計問題数
let correctCount = 0; // 正解数（点数として使用）
const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

function getQueryParam(param) {
  const params = new URLSearchParams(window.location.search);
  return params.get(param);
}

stompClient.connect({}, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/quiz/' + roomId, (response) => {
    quiz = JSON.parse(response.body);
    startQuiz(quiz);
  });
  startQuiz(quiz);
});

async function startQuiz(quiz) {
  try {
    const timeLimit = 12;
    const quizTimeLimit = 10;
    let remainingTime = timeLimit;
    let quizRemainingTime = quizTimeLimit;
    result.textContent = '';
    if (quiz.process >= totalQuestions + 1) {
      saveScoreToDatabase(playerName, correctCount);
      document.getElementById('quiz-container').innerHTML = `
            <h2>クイズ終了！</h2>
            <p>${playerName}の点数は ${correctCount} 点です</p>
          `;
      return;
    }
    // クイズデータをページに表示
    document.getElementById('word').textContent = quiz.word;
    const optionsContainer = document.getElementById('options');
    optionsContainer.innerHTML = ''; // 選択肢をクリア

    const quizTimer = setInterval(() => {
      quizRemainingTime--;

      if (quizRemainingTime <= 0) {
        clearInterval(quizTimer);
        for (let button of optionsContainer.getElementsByTagName('button')) {
          button.disabled = true;
        }
        if(result.textContent == ''){
          result.textContent = `不正解。正解は「${quiz.correctMean}」です。`;
          result.style.color = 'red';
        }
      }
    }, 1000);

    const polling = () => {
      remainingTime--;

      setTimeout(async () => {
        if (remainingTime <= 0) {
          remainingTime = timeLimit;
          const params = { roomid: roomId };
          const query = new URLSearchParams(params);
          const response = await fetch(`/api/quiz/count?${query}`,);
          const judge = await response.json();
          console.log(judge);
          if (judge) {
            fetchQuiz();
          }
        } else {
          polling()
        }
      }, 1000);
    }

    polling();

    quiz.options.forEach(option => {
      const button = document.createElement('button');
      button.textContent = option;
      button.onclick = () => checkAnswer(option, quiz.correctMean);
      optionsContainer.appendChild(button);
    });
  } catch (error) {
    console.error('クイズの取得中にエラーが発生しました:', error);
    document.getElementById('quiz-container').innerHTML = 'エラーが発生しました。リロードしてください。';
  }
}

async function checkAnswer(selected, correct) {
  const result = document.getElementById('result');
  const optionsContainer = document.getElementById('options');
  const buttons = optionsContainer.getElementsByTagName('button');

  // 全てのボタンを無効化
  for (let button of buttons) {
    button.disabled = true;
  }

  if (selected === correct) {
    correctCount += 10; // 1問ごとに10点加算
    result.textContent = '正解！🎉';
    result.style.color = 'green';
  } else {
    result.textContent = `不正解。正解は「${correct}」です。`;
    result.style.color = 'red';
  }

}

function fetchQuiz() {
  const params = { roomid: roomId };
  const query = new URLSearchParams(params);
  fetch(`/api/quiz?${query}`)
}

async function saveScoreToDatabase(userName, score) {
  try {
    console.log(`送信するデータ:`, { userName, score });
    const response = await fetch('/api/score', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ userName, score }),
    });
    if (!response.ok) {
      throw new Error('スコアの保存に失敗しました');
    }

    console.log('スコアが正常に保存されました');
  } catch (error) {
    console.error('スコア送信中にエラーが発生しました:', error);
  }
}
