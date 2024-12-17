package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import berry.tetra.service.QuizService;
import berry.tetra.model.QuizQuestion;

@RestController
@RequestMapping("/api/soloquiz")
public class SoloQuizController {

  @Autowired
  private QuizService quizService;

  /**
   * クイズデータを取得するエンドポイント
   *
   * @return QuizQuestion クイズデータ
   */
  @GetMapping
  public QuizQuestion getQuiz() {
    return quizService.generateQuiz();
  }
}
