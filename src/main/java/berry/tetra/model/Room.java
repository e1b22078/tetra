package berry.tetra.model;

public class Room {
  int roomId;
  int process;
  int count;
  int roomSize;

  public int getRoomId() {
    return roomId;
  }
  public int getRoomSize() {
    return roomSize;
  }
  public void setRoomSize(int roomSize) {
    this.roomSize = roomSize;
  }
  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }
  public int getCount() {
    return count;
  }
  public void setCount(int count) {
    this.count = count;
  }
  public int getProcess() {
    return process;
  }
  public void setProcess(int process) {
    this.process = process;
  }
}