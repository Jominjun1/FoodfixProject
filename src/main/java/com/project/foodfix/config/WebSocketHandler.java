package com.project.foodfix.config;

import io.micrometer.common.lang.NonNullApi;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

@Component
@NonNullApi
public class WebSocketHandler extends TextWebSocketHandler {

    private final SessionManager sessionManager;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public WebSocketHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        Map<String, Object> attributes = session.getAttributes();
        String user_id = (String) attributes.get("user_id");
        Long store_id = (Long) attributes.get("store_id");

        if (user_id != null) {
            sessionManager.addUserSession(user_id, session);
        } else if (store_id != null) {
            sessionManager.addStoreSession(store_id, session);
        }
        session.sendMessage(new TextMessage("연결"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        String user_id = (String) attributes.get("user_id");
        Long store_id = (Long) attributes.get("store_id");

        if (user_id != null) {
            sessionManager.removeUserSession(user_id);
        } else if (store_id != null) {
            sessionManager.removeStoreSession(store_id);
        }
        if (session.isOpen()) {
            session.sendMessage(new TextMessage("연결 해제"));
        }
    }

    public void sendPackingOrder(Long store_id, Integer time) {
        WebSocketSession storeSession = sessionManager.findStoreSession(store_id);
        sendMessage(storeSession, "포장 주문 접수");

        scheduler.schedule(() -> {
            if (storeSession.isOpen()) {
                try {
                    storeSession.close();
                } catch (IOException e) {
                    logger.error("오류 발생");
                }
            }
        }, time, TimeUnit.MINUTES);
    }
    public void sendReservationOrder(Long store_id) {
        WebSocketSession storeSession = sessionManager.findStoreSession(store_id);
        sendMessage(storeSession, "예약 주문 접수");
    }

    public void sendUpdateReservation(String user_id) {
        WebSocketSession userSession = sessionManager.findUserSession(user_id);
        sendMessage(userSession, "예약 주문 상태 변경");
    }

    public void sendUpdatePacking(String user_id) {
        WebSocketSession userSession = sessionManager.findUserSession(user_id);
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
