package com.anabada.neighbor.reply.repository;

import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MbReplyRepository extends ReplyRepository {


    @Override
    @Select("SELECT*FROM reply WHERE memberId = #{memberId}")//내가 작성한 댓글목록
    List<Reply> findMyReply(long memberId);

    @Select("select * from reply where postId = #{postId} order by reGroup, replyId")
    public List<Reply> list(long postId);

    @Select("select memberName from member where memberId = #{memberId}")
    public String findMemberName(long memberId);

    @Insert("insert into reply (memberId, postId, comment) values (#{memberId}, #{postId}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "replyId")
    public void write(Reply reply);

    @Update("update reply set reGroup = #{replyId} where replyId = #{replyId}")
    public void updateReGroup(Reply reply);

    @Update("update reply set comment = '' where replyId = #{replyId}")
    public void delete(long replyId);

    @Update("update reply set comment = #{comment}, replyUpdate = now() where replyId = #{replyId}")
    public void update(Reply reply);

    @Select("select * from reply where replyId = #{replyId}")
    public Reply findReply(long replyId);

    @Insert("insert into reply (memberId, postId, comment, parentId, depth, reGroup) values (#{memberId}, #{postId}, #{comment}, #{parentId}, 1, #{reGroup})")
    public void writeReReply(Reply reply);
}
