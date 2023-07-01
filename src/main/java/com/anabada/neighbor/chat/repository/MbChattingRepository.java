package com.anabada.neighbor.chat.repository;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMember;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MbChattingRepository extends ChattingRepository {

    @Override
    @Select("select * from chattingRoom where postId = #{postId} and creator = #{creator} and roomStatus = 'y'")
    ChattingRoom roomCheck(ChattingRoom chattingRoom);

    @Override
    @Insert("insert into chattingRoom (postId, creator, type) values (#{postId}, #{creator}, #{type})")
    @Options(useGeneratedKeys = true, keyProperty = "roomId")
    void insertRoom(ChattingRoom chattingRoom);

    @Override
    @Insert("insert into chattingMember (roomId, memberId) values (#{roomId}, #{memberId})")
    void insertChatMember(@Param("roomId") long roomId, @Param("memberId") long memberId);

    @Override
    @Insert("insert into chattingMessage (roomId, writer, content, messageType) values (#{roomId}, #{sender}, #{content}, #{messageType})")
    void insertMessage(Chat chat);

    @Override
    @Select("select content from chattingMessage where roomId = #{roomId} and type != 'LINE' order by messageId desc limit 1")
    String lastMessage(long roomId);

    @Override
    @Select("select * from chattingMessage where roomId = #{roomId} and messageId >= #{messageId} order by messageId desc")
    List<ChattingMessage> chattingMessageList(@Param("roomId") long roomId, @Param("memberId") long memberId, @Param("messageId") long messageId);

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
    @Select("select count(*) from chattingMember where roomId = #{roomId} and memberId = #{memberId} and chatMemberStatus = 'y'")
    int check(ChattingMember chattingMember);

    @Override
    @Select("select type from chattingRoom where roomId = #{roomId}")
    String findTypeByRoomId(long roomId);

    @Override
    @Select("select count(*) from chattingMember where roomId = #{roomId} and chatMemberStatus = 'y'")
    int chatMemberCount(long roomId);

    @Override
    @Update("update chattingMember set chatMemberStatus = 'n' where roomId = #{roomId} and memberId = #{memberId}")
    void chatOut(@Param("roomId") long roomId, @Param("memberId") long memberId);

    @Override
    @Select("select count(*) from chattingRoom where roomId = #{roomId} and creator = #{memberId}")
    int checkCreator(@Param("roomId") long roomId, @Param("memberId") long memberId);

    @Override
    @Update("update chattingRoom set roomStatus = 'n' where roomId = #{roomId}")
    void closeRoom(long roomId);

    @Override
    @Select("select * from chattingMember where roomId = #{roomId}")
    List<ChattingMember> findChatMemberByRoomId(long roomId);

    @Override
    @Select("select chatMemberStatus from chattingMember where roomId = #{roomId} and memberId = #{memberId}")
    String chatMemberStatus(@Param("roomId") long roomId, @Param("memberId") long memberId);

    @Override
    @Update("update chattingMember set chatMemberStatus = 'y' where roomId = #{roomId}")
    void updateStatus(long roomId);

    @Override
    @Select("select messageId from chattingMessage where writer = #{memberId} and roomId = #{roomId} and messageType = 'LINE' order by messageId desc limit 1")
    long findLineMessageId(@Param("roomId") long roomId, @Param("memberId") long memberId);
}
