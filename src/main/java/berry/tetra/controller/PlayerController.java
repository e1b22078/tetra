package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;
import java.util.List;

@Controller
public class PlayerController {

  @Autowired
  UserInfoMapper userInfoMapper;

  // 名前入力ページへのGETリクエスト
  @GetMapping("/name")
  public String name() {
    return "name.html";
  }

  // プレイヤー情報を表示するページ
  @GetMapping("/player")
  public String player(@RequestParam("playername") String playername, Model model) {
    // プレイヤー名をデータベースに保存
    UserInfo userInfo = new UserInfo();
    userInfo.setUserName(playername);
    userInfo.setRoomId(1);
    userInfoMapper.insertUserInfo(userInfo);

    // 登録されている全ユーザー情報を取得
    List<UserInfo> allUsers = userInfoMapper.selectAllUsers();

    // モデルにプレイヤー名とユーザーリストを追加
    model.addAttribute("playername", playername);
    model.addAttribute("allUsers", allUsers);

    return "player.html";
  }

  @GetMapping("/qmatch")
  public String qmatch(@RequestParam("playername") String playername, Model model) {
    int roomId, countRoomId;
    UserInfo userInfo = userInfoMapper.selectByname(playername);
    roomId = userInfoMapper.selectMaxRoomId();
    countRoomId = userInfoMapper.selectCountRoomId(roomId);

    if (countRoomId == 5) {
      roomId = roomId + 1;
    }
    userInfo.setRoomId(roomId);
    userInfoMapper.insertRoomId(userInfo);

    return "room.html";
  }
}
