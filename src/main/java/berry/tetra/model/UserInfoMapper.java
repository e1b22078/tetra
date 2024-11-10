package berry.tetra.model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserInfoMapper {

  @Select("SELECT id, userName FROM userinfo WHERE id = #{id}")
  UserInfo selectById(int id);

  @Select("SELECT id, userName FROM userinfo")  // すべてのユーザー情報を取得するSQL
  List<UserInfo> selectAllUsers();  // すべてのユーザーを返すメソッド

  @Insert("INSERT INTO userinfo (userName) VALUES (#{userName});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUserInfo(UserInfo userInfo);
}
