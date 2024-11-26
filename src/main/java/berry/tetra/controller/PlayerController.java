package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;

@Controller
public class PlayerController {

  @Autowired
  UserInfoMapper userInfoMapper;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

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

    // モデルにプレイヤー名とユーザーリストを追加
    model.addAttribute("playername", playername);

    // サーバからクライアントにユーザ一覧を送信
    messagingTemplate.convertAndSend("/topic/users", userInfoMapper.selectAllUsers());

    return "player.html";
  }

  @GetMapping("/qmatch")
  public String qmatch(@RequestParam("playername") String playername, Model model) {
    return "room.html";
  }

  @GetMapping("/player")
  public String playerPage() {
    // サーバからクライアントにユーザ一覧を送信
    messagingTemplate.convertAndSend("/topic/users", userInfoMapper.selectAllUsers());
    return "player.html";
  }

  @GetMapping("/game")
  public String game() {
    return "game.html";
  }
}
