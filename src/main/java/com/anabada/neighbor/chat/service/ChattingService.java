package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;

import java.util.List;

public interface ChattingService {

    /**
     * 채팅방 만들기
     */
    long openRoom(long postId, PrincipalDetails principalDetails, String type);

    /**
     * 메시지 보내기
     */
    void sendMessage(Chat chat, long memberId);

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
    void chatOut(Chat chat, long memberId);

    /**
     * 해당 채팅방에 사용자 목록을 가져옴
     */
    List<Member> chattingMemberList(long roomId);

    /**
     * 채팅방 입장 (모임 가입 시)
     */
    void chatJoin(Long postId, PrincipalDetails principalDetails);

    /**
     * postId 로 roomId 를 가져옴
     */
    long findRoomId(Long postId);

    /**
     * 채팅방 삭제
     */
    void deleteChatRoom(long roomId);

    boolean chatNotification(long memberId);

    void chatNotificationRemove(long roomId, long memberId);
}
