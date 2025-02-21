package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import berry.tetra.service.ScoreService;
import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
  @Autowired
  private ScoreService scoreService;

  @Autowired
  private UserInfoMapper userInfoMapper;

  @PostMapping
  public String saveScore(@RequestBody UserInfo userInfo) {
    userInfo.setTempScore(userInfo.getScore());
    userInfoMapper.updateTempScore(userInfo.getId(), userInfo.getScore());
    if (userInfo.getScore() > userInfoMapper.selectById(userInfo.getId()).getScore()) {
      boolean isUpdated = scoreService.updateScore(userInfo.getId(), userInfo.getScore());
      if (isUpdated) {
        return "スコアが正常に更新されました";
      } else {
        return "ユーザーが存在しないため、スコアの更新に失敗しました";
      }
    } else {
      return "スコアは更新されませんでした";
    }

  }

  @GetMapping("/judge")
  public String judgeScore(@RequestParam("roomId") int roomId) {
    List<UserInfo> users = userInfoMapper.selectAllByRoomId(roomId);
    List<UserInfo> highestScoreUsers = new ArrayList<>();
    int highestScore = 0;
    for (UserInfo user : users) {
      if (user.getTempScore() > highestScore) {
        highestScore = user.getTempScore();
        highestScoreUsers.clear();
        highestScoreUsers.add(user);
      } else if (user.getTempScore() == highestScore && user.getTempScore() != 0) {
        highestScoreUsers.add(user);
      }
    }
    return highestScoreUsers.stream().map(UserInfo::getUserName).collect(Collectors.joining(", "));
  }

}
