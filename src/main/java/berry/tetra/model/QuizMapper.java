package berry.tetra.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QuizMapper {
  @Select("SELECT word,hinsi FROM word WHERE id = #{id}")
  Quiz selectByid(int id);
}
