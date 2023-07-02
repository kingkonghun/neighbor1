package com.anabada.neighbor.chat.repository;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMember;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingRepository {

    /**
     * 채팅방 존재 여부
     */
    ChattingRoom roomCheck(ChattingRoom chattingRoom);

    /**
     * 채팅방 만들기
     */
    void insertRoom(ChattingRoom chattingRoom);

    /**
     * 채팅방에 사용자 입장
     */
    void insertChatMember(long roomId, long memberId);

    /**
     * 메시지 등록
     */
    void insertMessage(Chat chat);

    /**
     * 마지막 메시지 가져오기
     */
    String lastMessage(long roomId);

    /**
     * 메시지 리스트 가져오기
     */
    List<ChattingMessage> chattingMessageList(long roomId, long memberId, long messageId);

    /**
     * roomId 로 채팅방 정보 조회
     */
    ChattingRoom findChatRoomByRoomId(long roomId);

    /**
     * roomId 로 참가자 memberId 조회
     */
    List<Long> findChatMemberIdByRoomId(long roomId);

    /**
     * memberId 로 roomId 조회
     */
    List<Long> findRoomIdByMemberId(long memberId);

    /**
     * 해당 채팅방에 참가중인 사용자인지 확인
     */
    int check(ChattingMember chattingMember);

    /**
     * 채팅방의 type 가져오기( ex) "used", "club")
     */
    String findTypeByRoomId(long roomId);

    /**
     * 채팅방의 사용자 수 조회
     */
    int chatMemberCount(long roomId);

    /**
     * 채팅방 나가기(chattingMember 의 chatMemberStatus 를 'n' 으로 변경)
     */
    void chatOut(long roomId, long memberId);

    /**
     * 채팅방 퇴장 여부(사용자의 chatMemberStatus 확인)
     */
    String chatMemberStatus(long roomId, long memberId);

    /**
     * 해당 채팅방 안에 모든 사용자의 chatMemberStatus 를 'y' 로 변경
     */
    void updateStatus(long roomId);

    /**
     * 채팅 시작점 조회
     */
    long findLineMessageId(long roomId, long memberId);
}
