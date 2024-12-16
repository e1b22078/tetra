package berry.tetra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import berry.tetra.model.QuizMapper;
import berry.tetra.model.QuizQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuizService {

    @Autowired
    private QuizMapper quizMapper;

    // クイズを生成するメソッド
    public QuizQuestion generateQuiz() {
      try {
        // ランダムに単語を取得
        int randomId = quizMapper.selectByRandomId();
        String word = quizMapper.selectByWord(randomId);
        String correctMean = quizMapper.selectByMean(randomId);
        String hinsi = quizMapper.selectByHinsi(randomId);

        // ランダムな和訳（正解以外の選択肢を取得）
        List<String> randomMeans = quizMapper.selectRandomMeanByHinsi(hinsi, randomId, 4);

        // 重複を防ぐ
        Set<String> options = new LinkedHashSet<>(randomMeans);
        options.add(correctMean); // 正解を追加

        // 選択肢をシャッフル
        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);

        // クイズオブジェクトを作成して返却
        return new QuizQuestion(word, correctMean, shuffledOptions);
      } catch (Exception e) {
        System.err.println("クイズ生成中にエラーが発生しました: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("クイズの生成に失敗しました。後ほどお試しください。", e);
      }
    }
}
