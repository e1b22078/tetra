package berry.tetra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlayerController {

  @GetMapping("/name")
  public String name() {
    return "name.html";
  }

  @GetMapping("/player")
  public String player(@RequestParam("playername") String playername, Model model) {
    model.addAttribute("playername", playername);
    return "player.html";
  }
}
