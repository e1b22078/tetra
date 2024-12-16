const totalQuestions = 10; // 合計問題数
let correctCount = 0; // 正解数（点数として使用）
const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);
let roomId = getQueryParam("roomid"); // URLからroomIdを取得
let quiz = {}; // 問題を格納する変数

// クエリパラメータを取得する関数
function getQueryParam(param) {
  const params = new URLSearchParams(window.location.search);
  return params.get(param);
}

// WebSocket接続（マルチプレイヤー用）
if (roomId) {
  stompClient.connect({}, () => {
    console.log("Connected");
    stompClient.subscribe('/topic/quiz/' + roomId, (response) => {
      quiz = JSON.parse(response.body);
      startQuiz(quiz);
    });
    startQuiz(quiz); // 最初の問題
  });
} else {
  // ソロモードの場合はHTTPリクエストで問題を取得
  fetchQuiz();
}

function startQuiz(quiz) {
  try {
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

// 正誤判定を行う関数
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

  // ソロモードの場合、問題を取得
  if (!roomId) {
    fetchQuiz();
  }
}

// クイズを取得する関数
async function fetchQuiz() {
  const params = { roomid: roomId };
  const query = new URLSearchParams(params);
  const url = roomId ? `/api/quiz?${query}` : `/api/quiz/solo`;  // ソロモードの場合は別のエンドポイントを使う

  try {
    const response = await fetch(url);
    if (response.ok) {
      quiz = await response.json();
      startQuiz(quiz);
    } else {
      console.error('クイズの取得に失敗しました');
      document.getElementById('quiz-container').innerHTML = 'クイズの取得に失敗しました。';
    }
  } catch (error) {
    console.error('クイズ取得中にエラーが発生しました:', error);
    document.getElementById('quiz-container').innerHTML = 'エラーが発生しました。リロードしてください。';
  }
}

// スコアをデータベースに保存する関数
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
