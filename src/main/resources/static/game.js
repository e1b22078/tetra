const totalQuestions = 10; // 合計問題数
let correctCount = 0; // 正解数（点数として使用）
let timeoutId; // setTimeout の ID を保持する変数
const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  console.log("Connected");
  stompClient.subscribe('/topic/quiz/' + roomid, (response) => {
    const quiz = JSON.parse(response.body);
    startQuiz(quiz);
  });
  fetchQuiz();
});

async function startQuiz(quiz) {
  try {
    if (quiz.process >= totalQuestions) {
      document.getElementById('quiz-container').innerHTML = `
            <h2>クイズ終了！</h2>
            <p>あなたの点数は ${correctCount} 点です</p>
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

    // 5秒後に答えを表示し、次の問題へ進むボタンを表示
    setTimeout(() => {
      document.getElementById('answer').textContent = `答えは: ${quiz.correctMean}`;
      document.getElementById('answer').style.display = 'block';
    }, 5000);  // 5秒後

    setTimeout(() => fetchQuiz(), 2000);

  } catch (error) {
    console.error('クイズの取得中にエラーが発生しました:', error);
    document.getElementById('quiz-container').innerHTML = 'エラーが発生しました。リロードしてください。';
  }
}

function checkAnswer(selected, correct) {
  const result = document.getElementById('result');
  clearTimeout(timeoutId); // 解答ボタンが押された時点でタイマーをクリア

  if (selected === correct) {
    correctCount += 10; // 1問ごとに10点加算
    result.textContent = '正解！🎉';
    result.style.color = 'green';
    setTimeout(() => {
      fetchQuiz();
    }, 5000);
  } else {
    result.textContent = `不正解。正解は「${correct}」です。`;
    result.style.color = 'red';
  }

  // 次のクイズへ進むボタンを表示
  document.getElementById('next-button').style.display = 'block';
}

function loadNextQuiz() {
  const result = document.getElementById('result');
  const nextButton = document.getElementById('next-button');

  // 次の問題に進む前に前回の答えを非表示にする
  document.getElementById('answer').style.display = 'none';
  result.textContent = ''; // 前回の結果をクリア

  // 次の問題へ
  fetchQuiz();
  // 次の問題へ進むボタンを非表示にする
  nextButton.style.display = 'none';
}

function fetchQuiz() {
  const params = {roomid : roomid};
  const query = new URLSearchParams(params);
  fetch(`/api/quiz?${query}`)
}