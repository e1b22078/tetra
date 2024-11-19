package berry.tetra.model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

@Mapper
public interface UserInfoMapper {

  @Select("SELECT id, userName, roomId FROM userinfo WHERE userName = #{userName}")
  UserInfo selectByName(String userName);

  @Select("SELECT MAX(roomId) FROM userinfo")
  int selectMaxRoomId();

  @Select("SELECT COUNT(*) FROM userinfo WHERE roomId = #{roomId}")
  int selectCountRoomId(int roomId);

  @Select("SELECT id, userName FROM userinfo") // すべてのユーザー情報を取得するSQL
  ArrayList<UserInfo> selectAllUsers(); // すべてのユーザーを返すメソッド

  @Insert("INSERT INTO userinfo (userName, roomId) VALUES (#{userName}, #{roomId});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUserInfo(UserInfo userInfo);

  @Update("UPDATE userinfo SET roomId=#{roomId} WHERE ID = #{id}")
  void insertRoomId(UserInfo userInfo);

  @Delete("DELETE FROM userinfo WHERE id = #{id}")
    void deleteUserById(int id);
}
