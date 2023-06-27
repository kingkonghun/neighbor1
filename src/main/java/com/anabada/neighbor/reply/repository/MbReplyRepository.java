package com.anabada.neighbor.reply.repository;

import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MbReplyRepository extends ReplyRepository {


    @Override
    @Select("SELECT*FROM reply WHERE memberId = #{memberId} ORDER BY replyId desc LIMIT #{criteria.amount} OFFSET #{criteria.offset}")//내가 작성한 댓글목록
    List<Reply> findMyReply(Map<String,Object> map);

    @Override
    @Select("select * from reply where postId = #{postId} order by reGroup, replyId")
    public List<Reply> list(long postId);

    @Override
    @Select("select memberName from member where memberId = #{memberId}")
    public String findMemberName(long memberId);

    @Override
    @Insert("insert into reply (memberId, postId, comment) values (#{memberId}, #{postId}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "replyId")
    public void write(Reply reply);

    @Override
    @Update("update reply set reGroup = #{replyId} where replyId = #{replyId}")
    public void updateReGroup(Reply reply);

    @Override
    @Update("update reply set comment = '' where replyId = #{replyId}")
    public void delete(long replyId);

    @Override
    @Update("update reply set comment = #{comment}, replyUpdate = now() where replyId = #{replyId}")
    public void update(Reply reply);

    @Override
    @Select("select * from reply where replyId = #{replyId}")
    public Reply findReply(long replyId);

    @Override
    @Insert("insert into reply (memberId, postId, comment, parentId, depth, reGroup) values (#{memberId}, #{postId}, #{comment}, #{parentId}, 1, #{reGroup})")
    public void writeReReply(Reply reply);

    @Override
    @Select("SELECT count(*) FROM reply WHERE memberId=#{memberId}")
    int countMyReply(long memberId);
}
