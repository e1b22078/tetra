package berry.tetra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RuleController {

  @GetMapping("/rule")
  public String admin() {
    return "rule.html";
  }

}
