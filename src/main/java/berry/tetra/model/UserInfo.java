package berry.tetra.model;

public class UserInfo {
  int id;
  String userName;
  String psswd;
  int roomId;

  public int getRoomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

  // Thymeleafでフィールドを扱うためにはgetter/setterが必ず必要
  // vscodeのソースコード右クリック->ソースアクションでsetter/getterを簡単に追加できる
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPsswd() {
    return psswd;
  }

  public void setPsswd(String psswd) {
    this.psswd = psswd;
  }

}
