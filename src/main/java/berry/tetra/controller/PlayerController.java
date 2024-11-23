package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;
import berry.tetra.model.Room;
import java.util.List;

@Controller
public class PlayerController {

  @Autowired
  UserInfoMapper userInfoMapper;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  Room room;

  // 名前入力ページへのGETリクエスト
  @GetMapping("/name")
  public String name() {
    return "name.html";
  }

  // プレイヤー情報を表示するページ
  @PostMapping("/player")
  public String player(@RequestParam("playername") String playername,
      Model model) {
    // プレイヤー名をデータベースに保存
    UserInfo userInfo = new UserInfo();
    userInfo.setUserName(playername);
    userInfo.setRoomId(0);
    userInfoMapper.insertUserInfo(userInfo);

    // rooomにユーザを追加
    room.addUser(playername);

    // 登録されている全ユーザー情報を取得
    List<UserInfo> allUsers = userInfoMapper.selectAllUsers();
    model.addAttribute("allUsers", allUsers);

    // モデルにプレイヤー名とユーザーリストを追加
    model.addAttribute("playername", playername);

    // サーバからクライアントにユーザ一覧を送信
    messagingTemplate.convertAndSend("/topic/users", room.getUsers());

    return "player.html";
  }

  @GetMapping("/qmatch")
  public String qmatch(@RequestParam("playername") String playername, Model model) {
    room.addUser(playername);
    model.addAttribute("room", room);
    return "room.html";
  }

  @GetMapping("/player")
  public String playerPage() {
    return "player";
  }

  @GetMapping("/game")
  public String game() {
    return "game.html";
  }
}
