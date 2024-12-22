package org.fastcampus.chatservice.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    final Map<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 웹소켓 클라이언트가 서버로 연결이 된 이후에
        log.info("{} connected", session.getId());
        this.webSocketSessionMap.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 웹소켓 클라이언트에서 메세지가 왔을 때
        log.info("{} sent {}", session.getId(), message.getPayload());
        this.webSocketSessionMap.values().forEach(
                webSocketSession -> {
                    try {
                        log.info("message : " + message);
                        webSocketSession.sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 서버에 접속되있던 웹소켓 클라이언트가 연결이 끊겼을 때
        log.info("{} disconnected", session.getId());
        this.webSocketSessionMap.remove(session.getId());
    }
}
