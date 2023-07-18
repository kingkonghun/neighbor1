package com.anabada.neighbor.chat.handler;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.sun.security.auth.UserPrincipal;
import org.junit.Test;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Component
public class UserHandShackHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String memberId;
        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            memberId = String.valueOf(principalDetails.getMember().getMemberId());
        } else {
            memberId = UUID.randomUUID().toString();
        }
        System.out.println("memberId = " + memberId);
        return new UserPrincipal(memberId);
    }
}
