package com.anabada.neighbor.chat.repository;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingRepository {
    ChattingRoom roomCheck(ChattingRoom chattingRoom);

    void insertRoom(ChattingRoom chattingRoom);

    void insertMessage(Chat chat);

    List<ChattingRoom> chattingRoomList(long memberId);

    String lastMessage(long roomId);

    List<ChattingMessage> chattingMessageList(long roomId);

    ChattingRoom findChatRoomByRoomId(long roomId);
}
