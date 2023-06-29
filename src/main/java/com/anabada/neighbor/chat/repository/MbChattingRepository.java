package com.anabada.neighbor.chat.repository;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMember;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface MbChattingRepository extends ChattingRepository {

    @Override
    @Select("select * from chattingRoom where postId = #{postId} and creator = #{creator}")
    ChattingRoom roomCheck(ChattingRoom chattingRoom);

    @Override
    @Insert("insert into chattingRoom (postId, creator) values (#{postId}, #{creator})")
    @Options(useGeneratedKeys = true, keyProperty = "roomId")
    void insertRoom(ChattingRoom chattingRoom);

    @Override
    @Insert("insert into chattingMember (roomId, memberId) values (#{roomId}, #{memberId})")
    void insertChatMember(@Param("roomId") long roomId, @Param("memberId") long memberId);

    @Override
    @Insert("insert into chattingMessage (roomId, writer, content, messageType) values (#{roomId}, #{sender}, #{content}, #{messageType})")
    void insertMessage(Chat chat);

    @Override
    @Select("select content from chattingMessage where roomId = #{roomId} order by messageId desc limit 1")
    String lastMessage(long roomId);

    @Override
    @Select("select * from chattingMessage where roomId = #{roomId} order by messageId desc")
    List<ChattingMessage> chattingMessageList(long roomId);

    @Override
    @Select("select * from chattingRoom where roomId = #{roomId}")
    ChattingRoom findChatRoomByRoomId(long roomId);

    @Override
    @Select("select memberId from chattingMember where roomId = #{roomId}")
    List<Long> findChatMemberIdByRoomId(long roomId);

    @Override
    @Select("select roomId from chattingMember where memberId = #{memberId} order by roomId desc")
    List<Long> findRoomIdByMemberId(long memberId);

    @Override
    @Select("select count(*) from chattingMember where roomId = #{roomId} and memberId = #{memberId}")
    int check(ChattingMember chattingMember);
}
