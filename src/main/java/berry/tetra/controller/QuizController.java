package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import berry.tetra.service.QuizService;
import berry.tetra.model.QuizQuestion;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

  @Autowired
  private QuizService quizService;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  /**
   * クイズデータを取得するエンドポイント
   *
   * @return QuizQuestion クイズデータ
   */
  @GetMapping
  public QuizQuestion getQuiz(@RequestParam("roomid") int roomId) {
    QuizQuestion quiz= quizService.generateQuiz();
    messagingTemplate.convertAndSend("/topic/quiz/" + roomId, quiz);
    return quiz;
  }
}
