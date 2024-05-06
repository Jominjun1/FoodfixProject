package com.project.foodfix.config;

import com.project.foodfix.service.SessionService;
import io.micrometer.common.lang.NonNullApi;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@NonNullApi
public class WebSocketHandler extends TextWebSocketHandler {

    private final SessionService sessionService;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public WebSocketHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String session_id = session.getId();
        sessions.put(session_id, session);

        URI uri = session.getUri();
        assert uri != null;
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(uri).build().getQueryParams();

        String user_id = queryParams.getFirst("user_id");
        String store_idS = queryParams.getFirst("store_id");
        Long store_id = null;
        logger.info("id들: {} , {} ", user_id, store_idS);
        if (store_idS != null) {
            try { store_id = Long.parseLong(store_idS);
            } catch (NumberFormatException e) { logger.error("오류"); }
        }
        if (user_id != null) {
            sessionService.addUserSession(user_id, session);
        } else if (store_id != null) {
            sessionService.addStoreSession(store_id, session);
        }
        sendMessage(session, session_id);
        logger.info("연결된 세션: {}", session_id);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        String session_id = session.getId();
        sessions.remove(session_id);
        logger.info("삭제된 세션: {}", session_id);
        sessionService.removeSession(session_id);
        logger.info("닫힌 세션: {}", session_id);
    }

    public void sendPacking(Long store_id, String message) {
        Optional<String> storeSS_id = sessionService.findStoreSession(store_id);
        if (storeSS_id.isPresent()) {
            String storeSS = storeSS_id.get();
            WebSocketSession storeSession = sessions.get(storeSS);
            if (storeSession != null) {
                sendMessage(storeSession, message);
            } else {
                logger.info("매장 세션 x");
            }
        } else {
            logger.info("매장 세션 못찾음");
        }
    }

    public void sendReservation(Long store_id, String message) {
        Optional<String> storeSS_id = sessionService.findStoreSession(store_id);
        if (storeSS_id.isPresent()) {
            String storeSS = storeSS_id.get();
            WebSocketSession storeSession = sessions.get(storeSS);
            if (storeSession != null) {
                sendMessage(storeSession, message);
            } else {
                logger.info("매장 세션 x");
            }
        } else {
            logger.info("매장 세션 못찾음");
        }
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

