package com.anabada.neighbor.reply.repository;

import com.anabada.neighbor.reply.domain.Reply;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository {
    public List<Reply> list(long postId);

    public String findMemberName(long memberId);

    public void write(Reply reply);

    public void updateReGroup(Reply reply);

    public void delete(long replyId);

    public void update(Reply reply);

    public void writeReReply(Reply reply);

    public Reply findReply(long replyId);



}
