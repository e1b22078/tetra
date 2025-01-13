package berry.tetra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import berry.tetra.model.UserInfoMapper;

@Service
public class ScoreService {
  @Autowired
  private UserInfoMapper userInfoMapper;

  // スコアを更新するメソッド
  public boolean updateScore(int id, int score) {
    int updatedRows = userInfoMapper.updateScore(id, score);
    return updatedRows > 0; // 更新が成功したかどうかを返す
  }
}
