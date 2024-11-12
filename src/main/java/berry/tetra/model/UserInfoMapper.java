package berry.tetra.model;

import org.apache.ibatis.annotations.Insert;
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
}
