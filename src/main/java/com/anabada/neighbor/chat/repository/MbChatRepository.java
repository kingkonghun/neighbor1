package com.anabada.neighbor.chat.repository;

import com.anabada.neighbor.chat.domain.ChatRoom;
import com.anabada.neighbor.chat.domain.ChatSession;
import org.apache.ibatis.annotations.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface MbChatRepository extends ChatRepository{
    @Override
    @Insert("INSERT INTO chatroom (memberId,roomNumber,roomName) values(#{memberId},#{roomNumber},#{roomName})")
    void createRoom(Map<String, Object> map);

    @Override
    @Select("SELECT roomNumber FROM chatRoom WHERE memberId=#{memberId}")
    List<ChatRoom> findRoomNumber(long memberId);

    @Override
    @Select("SELECT * FROM chatSession WHERE roomNumber=#{roomNumber}")
    List<ChatSession> getSessionIds(String roomNumber);



    @Insert("INSERT INTO chatSession (roomNumber, sessionId) VALUES (#{roomNumber}, #{sessionId})")
    void saveSessionInfo(Map<String, String> map);


    @Override
    @Delete("DELETE FROM chatSession WHERE sessionId=#{sessionId}")
    void removeSessionInfo(String sessionId);

    @Override
    @Select("SELECT * FROM chatSession WHERE sessionId=#{sessionId}")
    String findRoomNumberBySessionId(ChatSession sessionId);


}
