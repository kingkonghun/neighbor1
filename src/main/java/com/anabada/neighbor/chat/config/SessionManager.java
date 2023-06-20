package com.anabada.neighbor.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;


@Component
public class SessionManager {
    private Map<String, WebSocketSession> sessionMap;

    public SessionManager() {
        sessionMap = new HashMap<>();
    }

    public void addSession(String sessionId, WebSocketSession session) {
        if (session.isOpen()) {
            sessionMap.put(sessionId, session);
            System.out.println("session = " + session);
        } else {
            System.out.println("세션닫힘");
        } 
    }

    public WebSocketSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }
}
