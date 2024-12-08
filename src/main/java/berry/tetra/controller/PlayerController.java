package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

  // プレイヤー情報を表示するページ（POSTメソッド）
  @PostMapping("/player")
  public String player(@RequestParam("playername") String playername, Model model) {
    // プレイヤー名を使ってデータベースに新しいユーザー情報を追加する（仮の処理）
    UserInfo userInfo = new UserInfo();
    userInfo.setUserName(playername);
    userInfoMapper.insertUserInfo(userInfo);  // ここでプレイヤー情報をDBに追加

    // プレイヤーIDを取得（仮の方法）
    int id = userInfo.getId();  // 新しく挿入されたユーザーのIDを取得

    // プレイヤー名をモデルに設定
    model.addAttribute("playername", playername);
    model.addAttribute("id", id);  // idをHTMLに渡す

    // サーバからクライアントにユーザー一覧を送信
    messagingTemplate.convertAndSend("/topic/users", userInfoMapper.selectAllUsers());

    // プレイヤー情報を表示するページにリダイレクト
    return "player.html";
  }

  @GetMapping("/qmatch")
  public String qmatch(@RequestParam("id") int id, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id); // id でユーザー情報を取得
    int roomid = 1;
    int roomlimit = 2;
    while (userInfoMapper.selectCountRoomId(roomid) == roomlimit) {
      roomid++;
    }
    userInfo.setRoomId(roomid);
    userInfoMapper.insertRoomId(userInfo);
    model.addAttribute("roomid", roomid);
    model.addAttribute("playername", userInfo.getUserName());

    // サーバからクライアントにユーザー一覧を送信
    messagingTemplate.convertAndSend("/topic/roomusers", "reload");
    return "room.html";
  }

  @GetMapping("/game")
  public String game(@RequestParam("id") int id, @RequestParam("roomid") int roomId,
      @RequestParam(value = "trigger", defaultValue = "false") boolean trigger, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id); // id でユーザー情報を取得
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("roomid", roomId);
    if (!trigger) {
      messagingTemplate.convertAndSend("/topic/startGame/" + roomId, "gamestart");
    }
    return "game.html";
  }
}
