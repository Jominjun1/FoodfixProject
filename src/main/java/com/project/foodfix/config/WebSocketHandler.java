package com.project.foodfix.config;

import io.micrometer.common.lang.NonNullApi;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.logging.Logger;

@Component
@NonNullApi
public class WebSocketHandler extends TextWebSocketHandler {

    private final StoreSessionManager storeSessionManager;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketHandler.class);

    public WebSocketHandler(StoreSessionManager storeSessionManager) {
        this.storeSessionManager = storeSessionManager;
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            String message = "서버와 연결되었습니다";
            session.sendMessage(new TextMessage(message));
            logger.info("클라이언트와 연결 되었습니다.");
        } catch (IOException e) {
            logger.severe("클라 접속 오류: " + e.getMessage());
        }
    }
    public void sendPackingOrder(Long store_id) {
        WebSocketSession storeSession = storeSessionManager.findStoreId(store_id);

        if (storeSession != null && storeSession.isOpen()) {
            try {
                String message = "포장 주문이 접수되었습니다.";
                storeSession.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                logger.severe("포장 주문 처리 중 오류 발생: " + e.getMessage());
            }
        }
    }

    public void sendReservationOrder(Long store_id) {
        WebSocketSession storeSession = storeSessionManager.findStoreId(store_id);

        if (storeSession != null && storeSession.isOpen()) {
            try {
                String message = "예약 주문이 접수되었습니다.";
                storeSession.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                logger.severe("예약 주문 처리 중 오류 발생: " + e.getMessage());
            }
        }
    }
}
