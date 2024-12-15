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

    private final Map<String, Integer> groupConnections = new ConcurrentHashMap<>();
    private final Map<String, String> sessionGroupMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String roomId = headerAccessor.getFirstNativeHeader("roomid");

        if (sessionId != null && roomId != null) {
            sessionGroupMap.put(sessionId, roomId);

            groupConnections.merge(roomId, 1, Integer::sum);

            notifyGroupConnectionCount(roomId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        if (sessionId != null && sessionGroupMap.containsKey(sessionId)) {
            String roomId = sessionGroupMap.remove(sessionId);

            groupConnections.merge(roomId, -1, (oldVal, val) -> Math.max(oldVal + val, 0));

            notifyGroupConnectionCount(roomId);
        }
    }

    private void notifyGroupConnectionCount(String roomId) {
        int count = groupConnections.getOrDefault(roomId, 0);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, count);
    }
}