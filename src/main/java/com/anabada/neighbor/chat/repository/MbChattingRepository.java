package com.anabada.neighbor.chat.repository;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMember;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface MbChattingRepository extends ChattingRepository {

    @Override
    @Select("select * from chattingRoom where postId = #{postId} and creator = #{creator}")
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
    @Select("select content from chattingMessage where roomId = #{roomId} and messageType != 'LINE' order by messageId desc limit 1")
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
    @Update("update chattingMember set chatMemberStatus = #{status} where roomId = #{roomId} and memberId = #{memberId}")
    void updateStatus(@Param("roomId") long roomId, @Param("memberId") long memberId, @Param("status") String status);

    @Override
    @Select("select chatMemberStatus from chattingMember where roomId = #{roomId} and memberId = #{memberId}")
    String chatMemberStatus(@Param("roomId") long roomId, @Param("memberId") long memberId);

    @Override
    @Update("update chattingMember set chatMemberStatus = #{status} where roomId = #{roomId}")
    void updateStatusAll(@Param("roomId") long roomId, @Param("status") String status);

    @Override
    @Select("select messageId from chattingMessage where writer = #{memberId} and roomId = #{roomId} and messageType = 'LINE' order by messageId desc limit 1")
    long findLineMessageId(@Param("roomId") long roomId, @Param("memberId") long memberId);

    @Override
    @Select("select roomId from chattingRoom where postId = #{postId}")
    long findRoomIdByPostId(Long postId);

    @Override
    @Select("select count(*) from chattingMember where roomId = #{roomId} and memberId = #{memberId}")
    int checkJoin(ChattingMember build);

    @Override
    @Select("select memberId from chattingMember where roomId = #{roomId} and chatMemberStatus = 'y'")
    List<Long> findStatusYMemberIdByRoomId(long roomId);

    @Override
    @Update("update chattingRoom set type = 'del' where roomId = #{roomId}")
    void deleteChatRoom(long roomId);
}
