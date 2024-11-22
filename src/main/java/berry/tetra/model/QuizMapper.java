package berry.tetra.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QuizMapper {
  @Select("SELECT word FROM word WHERE id = #{id}")
  String selectByWord(int id);

  @Select("SELECT mean FROM word WHERE id = #{id}")
  String selectByMean(int id);

  @Select("SELECT hinsi FROM word WHERE id = #{id}")
  String selectByHinsi(int id);

  @Select("SELECT id FROM word ORDER BY RAND() LIMIT 1")
  int selectByRandomId();

  @Select("SELECT mean FROM word WHERE hinsi = #{hinsi} AND id != #{id} ORDER BY RANDOM() LIMIT #{limit}")
  List<String> selectRandomMeanByHinsi(String hinsi, int id, int limit);
}
