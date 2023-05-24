package com.anabada.neighbor.reply.service;

import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;
import com.anabada.neighbor.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        for (Reply reply : replyRepository.list(postId)) {
            String memberName = replyRepository.findMemberName(reply.getMemberId());
            CarryReply carryReply = new CarryReply(reply.getReplyId(), reply.getMemberId(), reply.getPostId(), reply.getComment(), reply.getReplyDate(),
                    reply.getReplyUpdate(), reply.getParentId(), reply.getDepth(), memberName);
            list.add(carryReply);
        }
        return list;
    }

    @Override
    public void write(Reply reply) {
        replyRepository.write(reply);
    }
}
