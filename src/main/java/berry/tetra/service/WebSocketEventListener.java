package berry.tetra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import berry.tetra.model.UserInfoMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {
  @Autowired
  UserInfoMapper userInfoMapper;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  private final Map<String, Integer> roomConnections = new ConcurrentHashMap<>();
  private final Map<String, String> sessionRoomMap = new ConcurrentHashMap<>();
  private final Map<String, Integer> playerConnections = new ConcurrentHashMap<>();
  private final Map<String, String> sessionPlayerMap = new ConcurrentHashMap<>();

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    String roomId = headerAccessor.getFirstNativeHeader("roomId");
    String userId = headerAccessor.getFirstNativeHeader("userId");

    if (sessionId != null && roomId != null) {
      sessionRoomMap.put(sessionId, roomId);

      roomConnections.merge(roomId, 1, Integer::sum);

      notifyRoomConnectionCount(roomId);
    }

    if (sessionId != null && userId != null) {
      sessionPlayerMap.put(sessionId, userId);

      playerConnections.merge(userId, 1, Integer::sum);

      notifyPlayerConnectionCount(userId);
    }
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();

    if (sessionId != null && sessionRoomMap.containsKey(sessionId)) {
      String roomId = sessionRoomMap.remove(sessionId);

      roomConnections.merge(roomId, -1, (oldVal, val) -> Math.max(oldVal + val, 0));

      notifyRoomConnectionCount(roomId);
    }

    if (sessionId != null && sessionPlayerMap.containsKey(sessionId)) {
      String userId = sessionPlayerMap.remove(sessionId);

      roomConnections.merge(userId, -1, (oldVal, val) -> Math.max(oldVal + val, 0));

      notifyPlayerConnectionCount(userId);
    }
  }

  private void notifyRoomConnectionCount(String roomId) {
    int count = roomConnections.getOrDefault(roomId, 0);
    messagingTemplate.convertAndSend("/topic/room/" + roomId, count);
  }

  private void notifyPlayerConnectionCount(String userId) {
    int count = roomConnections.getOrDefault(userId, 0);
    messagingTemplate.convertAndSend("/topic/users/", count);
  }
}
