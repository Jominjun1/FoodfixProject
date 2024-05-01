package com.project.foodfix.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StoreSessionManager {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final Map<Long, WebSocketSession> storeSessions = new ConcurrentHashMap<>();

    public void addUserSession(String user_id, WebSocketSession session) {
        userSessions.put(user_id, session);
    }

    public void removeUserSession(String user_id) {
        userSessions.remove(user_id);
    }
    public void addStoreSession(Long store_id, WebSocketSession session) {
        storeSessions.put(store_id, session);
    }

    public void removeStoreSession(Long store_id) {
        storeSessions.remove(store_id);
    }

    public WebSocketSession findUserSession(String user_id) {
        return userSessions.get(user_id);
    }

    public WebSocketSession findStoreSession(Long store_id) {
        return storeSessions.get(store_id);
    }
}


