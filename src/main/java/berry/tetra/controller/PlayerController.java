package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;

@Controller
public class PlayerController {
  @Autowired
  UserInfoMapper userInfoMapper;

  @GetMapping("/name")
  public String name() {
    return "name.html";
  }

  @GetMapping("/player")
  public String player(@RequestParam("playername") String playername, Model model) {
    UserInfo userInfo = new UserInfo();
    userInfo.setUserName(playername);
    userInfoMapper.insertUserInfo(userInfo);
    model.addAttribute("playername", playername);
    return "player.html";
  }
}
