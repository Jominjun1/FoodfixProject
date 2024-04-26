package com.project.foodfix.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StoreSessionManager {

    private final Map<Long, WebSocketSession> storeSessions = new ConcurrentHashMap<>();

    public void addStoreSession(Long store_id, WebSocketSession session) {
        storeSessions.put(store_id, session);
    }
    public WebSocketSession findStoreId(Long store_id) {
        return storeSessions.get(store_id);
    }
}

