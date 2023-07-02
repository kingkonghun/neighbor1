package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.config.auth.PrincipalDetails;

import java.security.Principal;
import java.util.List;

public interface ChattingService {

    /**
     * 채팅방 만들기
     */
    long openRoom(long postId, PrincipalDetails principalDetails, String type);

    /**
     * 메시지 보내기
     */
    void sendMessage(Chat chat, Principal principal);

    /**
     * 채팅방 목록
     */
    List<Chat> chattingRoomList(PrincipalDetails principalDetails);

    /**
     * 채팅 메시지 목록
     */
    List<Chat> chattingMessageList(long roomId, PrincipalDetails principalDetails, String type);

    /**
     * 1대1 채팅 상대방 정보
     */
    Chat getReceiver(long roomId, PrincipalDetails principalDetails);

    /**
     * 채팅방 입장 권한 체크
     */
    boolean check(long roomId, PrincipalDetails principalDetails);

    /**
     * 채팅방 나가기
     */
    void chatOut(long roomId, String type, PrincipalDetails principalDetails);
}
