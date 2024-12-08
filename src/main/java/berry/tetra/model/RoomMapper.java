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

  @Update("UPDATE room SET process=#{process} WHERE roomId = #{roomId}")
  void updateProcess(Room room);

  @Update("UPDATE room SET count=#{count} WHERE roomId = #{roomId}")
  void updateCount(Room room);

  @Update("UPDATE room SET roomSize=#{roomSize} WHERE roomId = #{roomId}")
  void updateRoomSize(Room room);
}
