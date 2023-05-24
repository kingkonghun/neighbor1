package com.anabada.neighbor.reply.repository;

import com.anabada.neighbor.reply.domain.Reply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MbReplyRepository extends ReplyRepository {

    @Select("select * from reply where postId = #{postId}")
    public List<Reply> list(long postId);

    @Select("select memberName from member where memberId = #{memberId}")
    public String findMemberName(long memberId);

    @Insert("insert into reply (memberId, postId, comment) values (#{memberId}, #{postId}, #{comment})")
    public void write(Reply reply);
}
