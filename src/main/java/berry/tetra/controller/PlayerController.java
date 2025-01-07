package berry.tetra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import berry.tetra.model.QuizQuestion;
import berry.tetra.model.Room;
import berry.tetra.model.RoomMapper;
import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;
import berry.tetra.service.QuizService;

@Controller
public class PlayerController {

  @Autowired
  private UserInfoMapper userInfoMapper;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  private RoomMapper roomMapper;

  @Autowired
  private QuizService quizService;

  // 名前入力ページへのGETリクエスト
  @GetMapping("/name")
  public String name() {
    return "name.html";
  }

  // プレイヤー情報を表示するページ（POSTメソッド）
  @PostMapping("/player")
  public String player(@RequestParam("playername") String playername, Model model) {
    // プレイヤー名を使ってデータベースに新しいユーザー情報を追加する（仮の処理）
    UserInfo userInfo = new UserInfo();
    userInfo.setUserName(playername);
    userInfoMapper.insertUserInfo(userInfo); // ここでプレイヤー情報をDBに追加

    // プレイヤーIDを取得（仮の方法）
    int id = userInfo.getId(); // 新しく挿入されたユーザーのIDを取得

    // プレイヤー名をモデルに設定
    model.addAttribute("playername", playername);
    model.addAttribute("id", id); // idをHTMLに渡す

    // サーバからクライアントにユーザー一覧を送信
    messagingTemplate.convertAndSend("/topic/users", userInfoMapper.selectAllUsers());

    // プレイヤー情報を表示するページにリダイレクト
    return "player.html";
  }

  @GetMapping("/sologame")
  public String sologame(@RequestParam("id") int id, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id);
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("id", userInfo.getId());
    return "soloGame.html";
  }

  @GetMapping("/qmatch")
  public String qmatch(@RequestParam("id") int id, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id); // id でユーザー情報を取得
    int roomid = 1;
    int roomlimit = 3;

    while (userInfoMapper.selectCountRoomId(roomid) == roomlimit) {
      roomid++;
    }
    if (roomMapper.selectCountRoomId(roomid) == 0) {
      Room room = new Room();
      room.setRoomId(roomid);
      room.setProcess(0);
      room.setCount(0);
      room.setRoomSize(1);
      roomMapper.insertRoom(room);
    } else {
      Room room = roomMapper.selectByRoomId(roomid);
      room.setRoomSize(room.getRoomSize() + 1);
      roomMapper.updateRoom(room);
    }
    userInfo.setRoomId(roomid);
    userInfoMapper.insertRoomId(userInfo);
    model.addAttribute("roomid", roomid);
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("id", userInfo.getId());

    return "room.html";
  }

  @GetMapping("/player")
  public String showPlayer(@RequestParam("id") int id, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id);
    if (userInfo.getRoomId() != 0) {
      Room room = roomMapper.selectByRoomId(userInfo.getRoomId());
      room.setProcess(0);
      room.setCount(0);
      room.setRoomSize(room.getRoomSize() - 1);
      roomMapper.updateRoom(room);
    }
    userInfo.setRoomId(0);
    userInfoMapper.insertRoomId(userInfo);

    // モデルにプレイヤー名
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("id", userInfo.getId());

    return "player.html";
  }

  @GetMapping("/game")
  public String game(@RequestParam("id") int id, @RequestParam("roomid") int roomId,
      @RequestParam("word") String word, @RequestParam("correctMean") String correctMean,
      @RequestParam List<String> options, Model model) {

    UserInfo userInfo = userInfoMapper.selectById(id);
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("id", userInfo.getId());
    model.addAttribute("roomid", roomId);
    model.addAttribute("quiz", new QuizQuestion(word, correctMean, options));
    return "game.html";
  }

  @GetMapping("/init")
  public void init(@RequestParam("roomid") int roomId) {
    QuizQuestion quiz = quizService.generateQuiz();
    Room room = roomMapper.selectByRoomId(roomId);
    int process = room.getProcess() + 1;
    room.setProcess(process);
    roomMapper.updateRoom(room);
    quiz.setProcess(process);
    messagingTemplate.convertAndSend("/topic/startGame/" + roomId, quiz);
  }

  @GetMapping("/ranking")
  public String ranking(Model model) {
    model.addAttribute("users", userInfoMapper.selectAllRanking());
    return "ranking.html";
  }
}
