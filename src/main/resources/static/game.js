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

function startQuiz(quiz) {
  try {
    result.textContent = '';
    if (quiz.process >= totalQuestions + 2) {
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

  if (selected === correct) {
    correctCount += 10; // 1問ごとに10点加算
    result.textContent = '正解！🎉';
    result.style.color = 'green';
  } else {
    result.textContent = `不正解。正解は「${correct}」です。`;
    result.style.color = 'red';
  }

  const params = { roomid: roomId };
  const query = new URLSearchParams(params);
  const response = await fetch(`/api/quiz/count?${query}`,);
  const judge = await response.json();
  console.log(judge);
  if (judge) {
    fetchQuiz();
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
