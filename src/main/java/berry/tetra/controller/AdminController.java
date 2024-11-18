package berry.tetra.controller;

import berry.tetra.model.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserInfoMapper userInfoMapper;

    // 管理者ページ表示
    @GetMapping
    public String admin(Model model) {
        model.addAttribute("users", userInfoMapper.selectAllUsers());
        return "admin.html";
    }

    // ユーザー削除処理
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        // ユーザーを削除する
        userInfoMapper.deleteUserById(id);
        return "redirect:/admin"; // 削除後に管理者ページにリダイレクト
    }
}
