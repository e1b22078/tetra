package berry.tetra.model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface RoomMapper {
  @Select("SELECT COUNT(*) FROM room WHERE roomId = #{roomId}")
  int selectCountRoomId(int roomId);

  @Select("SELECT process FROM room WHERE roomId = #{roomId}")
  int selectProcess(int roomId);

  @Insert("INSERT INTO room (roomId, process) VALUES (#{roomId}, #{process});")
  void insertRoom(Room room);

  @Update("UPDATE room SET process=#{process} WHERE roomId = #{roomId}")
  void updateProcess(Room room);
}
