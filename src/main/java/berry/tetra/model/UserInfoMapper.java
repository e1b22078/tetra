package berry.tetra.model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserInfoMapper {

  @Select("SELECT id, userName FROM userinfo WHERE userName = #{userName}")
  UserInfo selectByName(String userName);

  @Select("SELECT id, userName FROM userinfo") // すべてのユーザー情報を取得するSQL
  List<UserInfo> selectAllUsers(); // すべてのユーザーを返すメソッド

  @Select("SELECT id, userName FROM userinfo WHERE userName = #{userName} AND psswd = #{psswd}")
  UserInfo selectByNamePsswd(String userName, String psswd);

  @Insert("INSERT INTO userinfo (userName, psswd) VALUES (#{userName}, #{psswd});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUserInfo(UserInfo userInfo);

  @Select("SELECT id, userName, psswd FROM userinfo WHERE id = #{id}")
  UserInfo selectById(int id);

  @Delete("DELETE FROM userinfo WHERE id = #{id}")
  void deleteUserById(int id);

  @Update("UPDATE userinfo SET userName = #{userName}, psswd = #{psswd} WHERE id = #{id}")
  void updateUser(UserInfo userInfo);

  @Select("SELECT * FROM user_info WHERE user_name = #{userName}")
  UserInfo selectByUserName(String userName);
}
