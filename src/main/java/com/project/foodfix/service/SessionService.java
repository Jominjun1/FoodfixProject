package com.project.foodfix.service;

import com.project.foodfix.model.WebSession;
import com.project.foodfix.repository.WebSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Service
public class SessionService {

    private final WebSessionRepository webSessionRepository;

    @Autowired
    public SessionService(WebSessionRepository webSessionRepository) {
        this.webSessionRepository = webSessionRepository;
    }

    public void addUserSession(String user_id, WebSocketSession session) {
        WebSession webSession = new WebSession();
        webSession.setSession_id(session.getId());
        webSession.setUser_id(user_id);
        webSessionRepository.save(webSession);
    }

    public void addStoreSession(Long store_id, WebSocketSession session) {
        WebSession webSession = new WebSession();
        webSession.setSession_id(session.getId());
        webSession.setStore_id(store_id);
        webSessionRepository.save(webSession);
    }
    public void removeSession(String session_id){
        webSessionRepository.deleteBySession(session_id);
    }
    public Optional<String> findStoreSession(Long store_id) {
        return webSessionRepository.findByStoreId(store_id);
    }
    public Optional<String> findUserSession(String user_id) {
        return webSessionRepository.findByUserId(user_id);
    }
}
