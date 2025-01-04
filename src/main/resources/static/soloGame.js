let currentQuestion = 0; // 現在の問題数
let totalQuestions = 0; // ユーザーが選択した問題数
let correctCount = 0; // 正解数

function startQuiz() {
  // ユーザーが選択した問題数を取得
  const questionCountSelect = document.getElementById('question-count');
  totalQuestions = parseInt(questionCountSelect.value, 10);

  // 設定画面を非表示にしてクイズ画面を表示
  document.getElementById('setup-container').style.display = 'none';
  document.getElementById('quiz-container').style.display = 'block';

  // 最初のクイズを取得
  fetchQuiz();
}

async function fetchQuiz() {
  try {
    const response = await fetch('/api/soloquiz'); // クイズAPIのエンドポイント
    const quiz = await response.json();

    document.getElementById('word').textContent = quiz.word;
    const optionsContainer = document.getElementById('options');
    optionsContainer.innerHTML = ''; // 選択肢をクリア

    quiz.options.forEach(option => {
      const button = document.createElement('button');
      button.textContent = option;
      button.onclick = () => {
        document.querySelectorAll('.options button').forEach(btn => btn.disabled = true); // 選択肢を無効化
        checkAnswer(option, quiz.correctMean);
      };
      optionsContainer.appendChild(button);
    });
  } catch (error) {
    console.error('クイズの取得中にエラーが発生しました:', error);
    document.getElementById('quiz-container').innerHTML = 'エラーが発生しました。リロードしてください。';
  }
}

function checkAnswer(selected, correct) {
  const result = document.getElementById('result');
  if (selected === correct) {
    correctCount++;
    result.textContent = '正解！🎉';
    result.style.color = 'green';
  } else {
    result.textContent = `不正解。正解は「${correct}」です。`;
    result.style.color = 'red';
  }

  currentQuestion++;

  const nextButton = document.getElementById('next-button');
  if (currentQuestion < totalQuestions) {
    nextButton.textContent = '次の問題';
    nextButton.style.display = 'block';
  } else {
    nextButton.textContent = '結果を見る';
    nextButton.style.display = 'block';
  }
}

function loadNextQuiz() {
  const result = document.getElementById('result');
  const nextButton = document.getElementById('next-button');

  if (currentQuestion >= totalQuestions) {
    showResults();
    return;
  }

  // 次の問題
  result.textContent = '';
  nextButton.style.display = 'none';
  fetchQuiz();
}

function showResults() {
  const percentage = (correctCount / totalQuestions) * 100;
  let message = '';

  // メッセージを正解率に基づいて設定
  if (percentage <= 40) {
    message = 'もう少し勉強しましょう！';
  } else if (percentage <= 70) {
    message = 'よく頑張りました！あと少しで完璧です！';
  } else {
    message = '素晴らしい！完璧な成績ですね！';
  }

  document.getElementById('quiz-container').innerHTML = `
      <h2>クイズ終了！</h2>
      <p>あなたの結果は、${totalQuestions}問中${correctCount}問正解です。</p>
      <p>${message}</p>
    `;
}
