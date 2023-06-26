package com.anabada.neighbor.chat.repository;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MbChattingRepository extends ChattingRepository {

    @Override
    @Select("select * from chattingRoom where postId = #{postId} and sender = #{sender}")
    ChattingRoom roomCheck(ChattingRoom chattingRoom);

    @Override
    @Insert("insert into chattingRoom (postId,receiver, sender) values (#{postId}, #{receiver}, #{sender})")
    @Options(useGeneratedKeys = true, keyProperty = "roomId")
    void insertRoom(ChattingRoom chattingRoom);

    @Override
    @Insert("insert into chattingMessage (roomId, writer, content) values (#{roomId}, #{sender}, #{content})")
    void insertMessage(Chat chat);

    @Override
    @Select("select * from chattingRoom where receiver = #{memberId} or sender = #{memberId} order by roomId desc")
    List<ChattingRoom> chattingRoomList(long memberId);

    @Override
    @Select("select content from chattingMessage where roomId = #{roomId} order by messageId desc limit 1")
    String lastMessage(long roomId);

    @Override
    @Select("select * from chattingMessage where roomId = #{roomId} order by messageId asc")
    List<ChattingMessage> chattingMessageList(long roomId);

    @Override
    @Select("select * from chattingRoom where roomId = #{roomId}")
    ChattingRoom findChatRoomByRoomId(long roomId);
}
