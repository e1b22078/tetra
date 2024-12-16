package berry.tetra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import berry.tetra.model.QuizQuestion;
import berry.tetra.model.UserInfo;
import berry.tetra.model.UserInfoMapper;
import berry.tetra.service.QuizService;
import berry.tetra.model.Room;
import berry.tetra.model.RoomMapper;


@Controller
public class PlayerController {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private QuizService quizService;

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

        messagingTemplate.convertAndSend("/topic/users", userInfoMapper.selectAllUsers());

        return "player.html";
    }

    // クイズマッチの部屋に移動
    @GetMapping("/qmatch")
    public String qmatch(@RequestParam("id") int id, Model model) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        int roomid = 1;
        int roomlimit = 2;
        while (userInfoMapper.selectCountRoomId(roomid) == roomlimit) {
            roomid++;
        }
        if (roomMapper.selectCountRoomId(roomid) == 0) {
            Room room = new Room();
            room.setRoomId(roomid);
            room.setProcess(0);
            room.setCount(0);
            room.setRoomSize(1);
            roomMapper.insertRoom(room);
        } else {
            Room room = roomMapper.selectByRoomId(roomid);
            room.setRoomSize(room.getRoomSize() + 1);
            roomMapper.updateRoomSize(room);
        }
        userInfo.setRoomId(roomid);
        userInfoMapper.insertRoomId(userInfo);
        model.addAttribute("roomid", roomid);
        model.addAttribute("playername", userInfo.getUserName());
        model.addAttribute("id", userInfo.getId());

        messagingTemplate.convertAndSend("/topic/roomusers", "reload");
        return "room.html";
    }

    // プレイヤー画面を表示
    @GetMapping("/player")
    public String showPlayer(@RequestParam("id") int id, Model model) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        userInfo.setRoomId(0);
        userInfoMapper.resetRoomId(userInfo);

        model.addAttribute("playername", userInfo.getUserName());
        model.addAttribute("id", userInfo.getId());

        return "player.html";
    }

    // ゲーム画面を表示
    @GetMapping("/game")
    public String game(@RequestParam("id") int id, @RequestParam("roomid") int roomId,
        @RequestParam(value = "trigger", defaultValue = "false") boolean trigger, Model model) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        model.addAttribute("playername", userInfo.getUserName());
        model.addAttribute("id", userInfo.getId());
        model.addAttribute("roomid", roomId);
        if (!trigger) {
            messagingTemplate.convertAndSend("/topic/startGame/" + roomId, "gamestart");
        }
        return "game.html";
    }

    // ソロゲーム画面を表示
    @GetMapping("/sologame")
    public String soloGame(@RequestParam("id") int id, Model model) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        model.addAttribute("playername", userInfo.getUserName());
        model.addAttribute("id", userInfo.getId());
        model.addAttribute("roomid", 0);

        // クイズ取得処理
        try {
            QuizQuestion quiz = quizService.generateQuiz();  // QuizQuestion型を使用
            model.addAttribute("quiz", quiz);
        } catch (Exception e) {
            model.addAttribute("error", "クイズの取得に失敗しました。後ほどお試しください。");
        }

        return "sologame.html";
    }

    // ランキング画面を表示
    @GetMapping("/ranking")
    public String ranking(Model model) {
        model.addAttribute("users", userInfoMapper.selectAllRanking());
        return "ranking.html";
    }

    // API経由でソロクイズを取得
    @GetMapping("/api/quiz/solo")
    public QuizQuestion getSoloQuiz() {
        return quizService.generateQuiz();
    }
}
