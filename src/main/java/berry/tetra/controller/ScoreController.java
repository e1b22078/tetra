package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import berry.tetra.service.ScoreService;
import berry.tetra.model.UserInfo;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
  @Autowired
  private ScoreService scoreService;

  @PostMapping
  public String saveScore(@RequestBody UserInfo userInfo) {
    boolean isUpdated = scoreService.updateScore(userInfo.getUserName(), userInfo.getScore());

    if (isUpdated) {
      return "スコアが正常に更新されました";
    } else {
      return "ユーザーが存在しないため、スコアの更新に失敗しました";
    }
  }
}
