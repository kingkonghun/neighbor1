package com.anabada.neighbor.config.handler;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = "추가 정보를 입력 하신 후에 사용 하세요.";
        message = URLEncoder.encode(message,"utf-8");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (principalDetails.getMember().getRole().equals("ROLE_GUEST")) {
            response.sendRedirect("/member/editInfo?message="+message);
        } else if (principalDetails.getMember().getRole().equals("ROLE_USER")) {
            response.sendRedirect("/noAdmin");
        }
    }
}
