package berry.tetra.model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserInfoMapper {
  @Select("SELECT COUNT(*) FROM userinfo WHERE roomId = #{roomId} AND active = 0")
  int selectCountRoomId(int roomId);

  @Select("SELECT COUNT(*) FROM userinfo WHERE roomId = #{roomId} AND active = 1")
  int selectActiveCountRoomId(int roomId);

  @Select("SELECT * FROM userinfo WHERE roomId = #{roomId}")
  List<UserInfo> selectAllByRoomId(int roomId);

  @Select("SELECT * FROM userinfo")
  List<UserInfo> selectAllUsers();// すべてのユーザーを返すメソッド

  @Select("SELECT userName, score, RANK() OVER (ORDER BY score DESC) AS rank FROM userInfo ORDER BY rank LIMIT 5")
  List<UserInfo> selectAllRanking();

  @Insert("INSERT INTO userinfo (userName, roomId, active) VALUES (#{userName}, #{roomId}, #{active});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUserInfo(UserInfo userInfo);

  @Update("UPDATE userinfo SET roomId=#{roomId} WHERE id = #{id}")
  void insertRoomId(UserInfo userInfo);

  @Update("UPDATE userinfo SET active=#{active} WHERE id = #{id}")
  void activate(UserInfo userInfo);

  @Update("UPDATE userinfo SET score = #{score} WHERE userName = #{userName}")
  int updateScore(@Param("userName") String userName, @Param("score") int score);

  @Delete("DELETE FROM userinfo WHERE id = #{id}")
  void deleteUserById(int id);

  @Select("SELECT * FROM userinfo WHERE id = #{id}")
  UserInfo selectById(int id);
}
