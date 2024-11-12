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
    if (userInfoMapper.selectByName(playername) == null) {
      UserInfo userInfo = new UserInfo();
      userInfo.setUserName(playername);
      userInfoMapper.insertUserInfo(userInfo);
    } else {
      model.addAttribute("error", playername);
      return "name.html";
    }

    // 登録されている全ユーザー情報を取得
    List<UserInfo> allUsers = userInfoMapper.selectAllUsers();

    // モデルにプレイヤー名とユーザーリストを追加
    model.addAttribute("playername", playername);
    model.addAttribute("allUsers", allUsers);

    return "player.html";
  }
}
