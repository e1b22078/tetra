package berry.tetra.controller;

import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

  @Autowired
  private UserInfoMapper userInfoMapper;

  // ユーザー一覧を表示
  @GetMapping("/admin")
  public String admin(Model model) {
    model.addAttribute("users", userInfoMapper.selectAllUsers());
    return "admin";
  }

  // ユーザー削除
  @GetMapping("/admin/delete/{id}")
  public String deleteUser(@PathVariable("id") int id) {
    userInfoMapper.deleteUserById(id);
    return "redirect:/admin";  // 削除後にユーザー一覧ページにリダイレクト
  }
}
