package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import berry.tetra.model.Room;
import berry.tetra.model.RoomMapper;
import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;

@Controller
public class PlayerController {

  @Autowired
  private UserInfoMapper userInfoMapper;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  private RoomMapper roomMapper;

  // 名前入力ページへのGETリクエスト
  @GetMapping("/name")
  public String name() {
    return "name.html";
  }

  // プレイヤー情報を表示するページ（POSTメソッド）
  @PostMapping("/player")
  public String player(@RequestParam("playername") String playername, Model model) {
    UserInfo userInfo = new UserInfo();
    userInfo.setUserName(playername);
    userInfoMapper.insertUserInfo(userInfo);
    int id = userInfo.getId();

    model.addAttribute("playername", playername);
    model.addAttribute("id", id);

    return "player.html";
  }

  @GetMapping("/sologame")
  public String sologame(@RequestParam("id") int id, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id);
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("id", userInfo.getId());
    return "soloGame.html";
  }

  @GetMapping("/qmatch")
  public String qmatch(@RequestParam("id") int id, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id);
    int roomId = 1;
    int roomlimit = 2;

    while (userInfoMapper.selectCountRoomId(roomId) == roomlimit) {
      roomId++;
    }
    if (roomMapper.selectCountRoomId(roomId) == 0) {
      Room room = new Room();
      room.setRoomId(roomId);
      room.setProcess(0);
      room.setCount(0);
      room.setRoomSize(1);
      roomMapper.insertRoom(room);
    } else {
      Room room = roomMapper.selectByRoomId(roomId);
      room.setRoomSize(room.getRoomSize() + 1);
      roomMapper.updateRoom(room);
    }
    userInfo.setRoomId(roomId);
    userInfoMapper.insertRoomId(userInfo);
    model.addAttribute("roomId", roomId);
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("id", userInfo.getId());

    return "room.html";
  }

  @GetMapping("/player")
  public String showPlayer(@RequestParam("id") int id, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id);
    if (userInfo.getRoomId() != 0) {
      Room room = roomMapper.selectByRoomId(userInfo.getRoomId());
      room.setProcess(0);
      room.setCount(0);
      room.setRoomSize(room.getRoomSize() - 1);
      roomMapper.updateRoom(room);
    }
    userInfo.setRoomId(0);
    userInfoMapper.insertRoomId(userInfo);

    // モデルにプレイヤー名
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("id", userInfo.getId());

    return "player.html";
  }

  @GetMapping("/game")
  public String game(@RequestParam("id") int id, @RequestParam("roomId") int roomId, Model model) {
    UserInfo userInfo = userInfoMapper.selectById(id);
    model.addAttribute("playername", userInfo.getUserName());
    model.addAttribute("roomId", roomId);
    model.addAttribute("id", id);
    return "game.html";
  }

  @GetMapping("/init")
  public String init(@RequestParam("roomId") int roomId) {
    messagingTemplate.convertAndSend("/topic/startGame/" + roomId, "");
    return "init.html";
  }

  @GetMapping("/ranking")
  public String ranking(Model model) {
    model.addAttribute("users", userInfoMapper.selectAllRanking());
    return "ranking.html";
  }
}
