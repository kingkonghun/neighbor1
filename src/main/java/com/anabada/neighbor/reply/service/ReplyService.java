package com.anabada.neighbor.reply.service;

import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;

import java.util.List;

public interface ReplyService {
    public List<CarryReply> list(long postId);

    public void write(Reply reply);

    void delete(long replyId);

    void update(Reply reply);
}
