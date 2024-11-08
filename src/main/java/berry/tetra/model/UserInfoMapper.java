package berry.tetra.model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper {
  @Select("SELECT id,userName from userinfo where id = #{id}")
  UserInfo selectById(int id);

  @Insert("INSERT INTO userinfo (userName) VALUES (#{userName});")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUserInfo(UserInfo userInfo);
}
