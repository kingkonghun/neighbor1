package com.anabada.neighbor.reply.repository;

import com.anabada.neighbor.reply.domain.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MbReplyRepository extends ReplyRepository {

    @Select("select * from reply where postId = #{postId} order by parentId, depth")
    public List<Reply> list(long postId);

    @Select("select memberName from member where memberId = #{memberId}")
    public String findMemberName(long memberId);

    @Insert("insert into reply (memberId, postId, comment) values (#{memberId}, #{postId}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "replyId")
    public void write(Reply reply);

    @Delete("delete from reply where replyId = #{replyId}")
    public void delete(long replyId);

    @Update("update reply set comment = #{comment} where replyId = #{replyId}")
    public void update(Reply reply);

    @Select("select * from reply where replyId = #{replyId}")
    public Reply findReply(long replyId);

    @Insert("insert into reply (memberId, postId, comment, parentId, depth) values (#{memberId}, #{postId}, #{comment}, #{parentId}, #{depth})")
    public void writeReReply(Reply reply);

    @Update("update reply set parentId = #{replyId} where replyId = #{replyId}")
    public void updateParentId(Reply reply);

    @Update("update reply set depth=depth+1 where parentId=#{parentId} and depth >= #{depth}")
    public void updateDepth(Reply reply);
}
