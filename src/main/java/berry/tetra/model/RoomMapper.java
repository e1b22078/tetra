package berry.tetra.model;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomMapper {
  @Select("SELECT * FROM room WHERE roomId = #{roomId}")
  Room selectByRoomId(int roomId);

  @Select("SELECT COUNT(*) FROM room WHERE roomId = #{roomId}")
  int selectCountRoomId(int roomId);

  @Insert("INSERT INTO room (roomId, process, count, roomSize) VALUES (#{roomId}, #{process}, #{count}, #{roomSize});")
  void insertRoom(Room room);

  @Update("UPDATE room SET process=#{process}, count=#{count}, roomSize=#{roomSize} WHERE roomId = #{roomId}")
  void updateRoom(Room room);
}
