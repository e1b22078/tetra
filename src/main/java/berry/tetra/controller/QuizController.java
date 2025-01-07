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
  public void getQuiz(@RequestParam("roomid") int roomId) {
    QuizQuestion quiz = quizService.generateQuiz();
    Room room = roomMapper.selectByRoomId(roomId);
    int process = room.getProcess() + 1;
    room.setProcess(process);
    roomMapper.updateRoom(room);
    quiz.setProcess(process);
    messagingTemplate.convertAndSend("/topic/quiz/" + roomId, quiz);
  }

  @GetMapping("/count")
  public boolean setFailCount(@RequestParam("roomid") int roomId) {
    boolean result = false;
    Room room = roomMapper.selectByRoomId(roomId);

    room.setCount(room.getCount() + 1);
    System.out.println(room.getCount());
    if (room.getCount() == room.getRoomSize()) {
      room.setCount(0);
      result = true;
    }
    roomMapper.updateRoom(room);
    return result;
  }
}
