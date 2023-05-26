package com.anabada.neighbor.reply.service;

import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;
import com.anabada.neighbor.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public List<CarryReply> list(long postId) {
        List<CarryReply> list = new ArrayList<>();
        String parentName = null;
        for (Reply reply : replyRepository.list(postId)) {
            String memberName = replyRepository.findMemberName(reply.getMemberId());
            if(reply.getParentId() != 0) {
                Reply parent = replyRepository.findReply(reply.getParentId());
                parentName = replyRepository.findMemberName(parent.getMemberId());
            }
            System.out.println("parentName = " + parentName);
            CarryReply carryReply = new CarryReply(reply.getReplyId(), reply.getMemberId(), reply.getPostId(), reply.getComment(), reply.getReplyDate(),
                    reply.getReplyUpdate(), reply.getParentId(), reply.getDepth(), reply.getReGroup(), memberName, parentName);
            list.add(carryReply);
        }
        return list;
    }

    @Override
    public void write(Reply reply, HttpSession session) {
        reply.setMemberId((long)session.getAttribute("memberId"));
        replyRepository.write(reply);
        replyRepository.updateReGroup(reply);
    }

    @Override
    public void delete(long replyId) {
        replyRepository.delete(replyId);
    }

    @Override
    public void update(Reply reply) {
        replyRepository.update(reply);
    }

    @Override
    public void writeReReply(Reply reply, HttpSession session) {
        Reply parent = replyRepository.findReply(reply.getReplyId());

        reply.setMemberId((long)session.getAttribute("memberId"));

        reply.setPostId(parent.getPostId());

        reply.setReGroup(parent.getReGroup());

        reply.setParentId(parent.getReplyId());

        replyRepository.writeReReply(reply);

    }
}
