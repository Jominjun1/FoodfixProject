package com.project.foodfix.config;

import io.micrometer.common.lang.NonNullApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.Map;

@Component
@NonNullApi
public class WebSocketHandler extends TextWebSocketHandler {

    private final StoreSessionManager storeSessionManager;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    public WebSocketHandler(StoreSessionManager storeSessionManager) {
        this.storeSessionManager = storeSessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        Map<String, Object> attributes = session.getAttributes();
        String user_id = (String) attributes.get("user_id");
        Long store_id = (Long) attributes.get("store_id");

        if (user_id != null) {
            storeSessionManager.addUserSession(user_id, session);
        } else if (store_id != null) {
            storeSessionManager.addStoreSession(store_id, session);
        }
        session.sendMessage(new TextMessage("웹소켓 연결"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String user_id = (String) attributes.get("user_id");
        Long store_id = (Long) attributes.get("store_id");

        if (user_id != null) {
            storeSessionManager.removeUserSession(user_id);
        } else if (store_id != null) {
            storeSessionManager.removeStoreSession(store_id);
        }
        session.sendMessage(new TextMessage("웹소켓 연결 해제"));
    }

    public void sendPackingOrder(Long store_id) {
        WebSocketSession storeSession = storeSessionManager.findStoreSession(store_id);
        sendMessage(storeSession, "포장 주문 접수");
    }

    public void sendReservationOrder(Long store_id) {
        WebSocketSession storeSession = storeSessionManager.findStoreSession(store_id);
        sendMessage(storeSession, "예약 주문 접수");
    }

    public void sendUpdateReservation(String user_id) {
        WebSocketSession userSession = storeSessionManager.findUserSession(user_id);
        sendMessage(userSession, "예약 주문 상태 변경");
    }

    public void sendUpdatePacking(String user_id) {
        WebSocketSession userSession = storeSessionManager.findUserSession(user_id);
        sendMessage(userSession, "포장 주문 상태 변경");
    }

    public void sendMessage(WebSocketSession session, String message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                logger.error("오류 발생: {}", e.getMessage());
            }
        }
    }
}
