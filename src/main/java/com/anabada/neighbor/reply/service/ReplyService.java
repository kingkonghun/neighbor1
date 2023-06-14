package com.anabada.neighbor.reply.service;

import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ReplyService {
    public List<CarryReply> list(long postId);

    public void write(Reply reply, HttpSession session);

    public void delete(long replyId);

    public void update(Reply reply);

    public void writeReReply(Reply reply, HttpSession session);

}