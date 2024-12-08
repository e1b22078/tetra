package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import berry.tetra.service.QuizService;
import berry.tetra.model.QuizQuestion;
import berry.tetra.model.Room;
import berry.tetra.model.RoomMapper;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

  @Autowired
  private QuizService quizService;

  @Autowired
  private RoomMapper roomMapper;

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
    try {
      Room room = new Room();
      int process = roomMapper.selectProcess(roomId);
      process++;
      room.setRoomid(roomId);
      room.setProcess(process);
      roomMapper.updateProcess(room);
      quiz.setProcess(process);
      Thread.sleep(5000);
    } catch (Exception e) {
    }
    messagingTemplate.convertAndSend("/topic/quiz/" + roomId, quiz);
    return quiz;
  }

  @GetMapping("/correct")
  public void getMethodName(@RequestParam("") String param) {
    
  }
  
}
