package berry.tetra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;
import jakarta.servlet.http.HttpSession;

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
  public String player(@RequestParam("playername") String playername, HttpSession session, Model model) {
    session.setAttribute("playername", playername);
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
  public String qmatch(HttpSession session, Model model) {
    String playername = (String) session.getAttribute("playername");
    List<UserInfo> userInfos = userInfoMapper.selectAllByName(playername);
    UserInfo userInfo = userInfos.get(0);
    int roomid = 1;
    int roomlimit = 2;
    while (userInfoMapper.selectCountRoomId(roomid) == roomlimit) {
      roomid++;
    }
    userInfo.setRoomId(roomid);
    userInfoMapper.insertRoomId(userInfo);
    return "room.html";
  }

  @GetMapping("/player")
  public String showPlayer(HttpSession session) {
    String playername = (String) session.getAttribute("playername");
    userInfoMapper.resetRoomId(playername);
    // サーバからクライアントにユーザ一覧を送信
    messagingTemplate.convertAndSend("/topic/users", userInfoMapper.selectAllUsers());
    return "player.html";
  }

  @GetMapping("/game")
  public String game() {
    return "game.html";
  }
}
