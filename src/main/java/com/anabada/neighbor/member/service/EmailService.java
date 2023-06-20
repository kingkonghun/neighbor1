package com.anabada.neighbor.member.service;

public interface EmailService {
    /**
     * 이메일인증코드 전송하는 로직
     *
     */
    String sendSimpleMessage(String to) throws Exception;
}
