package berry.tetra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  UserInfoMapper userInfoMapper;

  @GetMapping
  public List<UserInfo> getUserInfo() {
    return userInfoMapper.selectAllUsers();
  }

}
