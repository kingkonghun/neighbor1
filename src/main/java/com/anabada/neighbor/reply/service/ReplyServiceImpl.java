package com.anabada.neighbor.reply.service;

import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;
import com.anabada.neighbor.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public List<CarryReply> list(long postId) { //댓글 목록
        List<CarryReply> list = new ArrayList<>(); //리턴할 값
        String parentName = null; //부모 닉네임
        for (Reply reply : replyRepository.list(postId)) { //파라미터로 받은 postId에 해당하는 댓글을 reply 테이블에서 가져오기
            String memberName = replyRepository.findMemberName(reply.getMemberId()); //reply 테이블의 memberId로 member 테이블에서 해당하는 memberName 가져오기
            if(reply.getParentId() != 0) { //reply 테이블의 parentId가 0이 아니라면(대댓글이라면)
                Reply parent = replyRepository.findReply(reply.getParentId()); //reply 테이블의 parentId로 reply 테이블에서 부모 튜플 가져오기
                parentName = replyRepository.findMemberName(parent.getMemberId()); //가져온 parent의 memberId로 member 테이블에서 해당하는 memberName 가져오기
            }
            CarryReply carryReply = CarryReply.builder() //carryReply 객체 생성 
                    .replyId(reply.getReplyId())
                    .memberId(reply.getMemberId())
                    .postId(reply.getPostId())
                    .comment(reply.getComment())
                    .replyDate(reply.getReplyDate())
                    .replyUpdate(reply.getReplyUpdate())
                    .parentId(reply.getParentId())
                    .depth(reply.getDepth())
                    .reGroup(reply.getReGroup())
                    .memberName(memberName)
                    .parentName(parentName)
                    .build();
            list.add(carryReply); //리턴할 list에 carryReply 추가
        }
        return list;
    }

    @Override
    public void write(Reply reply, HttpSession session) { //댓글 작성
        reply.setMemberId((long)session.getAttribute("memberId")); //session에 있는 memberId 가져와서 reply에 넣기
        replyRepository.write(reply); //댓글 작성
        replyRepository.updateReGroup(reply); //댓글 작성 후 해당하는 튜플의 reGroup 컬럼 업데이트
    }

    @Override
    public void delete(long replyId) { //댓글 삭제
        replyRepository.delete(replyId); //해당하는 튜플의 comment 컬럼을 ''(공백)으로 변경
    }

    @Override
    public void update(Reply reply) { //댓글 수정
        replyRepository.update(reply); //댓글 수정
    }

    @Override
    public void writeReReply(Reply reply, HttpSession session) { //대댓글 작성
        Reply parent = replyRepository.findReply(reply.getReplyId()); //reply에 담아온 부모의 replyId로 reply 테이블에서 해당하는 튜플 가져오기
        reply.setMemberId((long)session.getAttribute("memberId")); //session에서 현재 로그인 중인 memberId를 reply에 저장 
        reply.setPostId(parent.getPostId()); //부모 튜플의 postId를 reply에 저장
        reply.setReGroup(parent.getReGroup()); //부모 튜플의 reGroup을 reply에 저장해서 같은 그룹으로 묶기
        reply.setParentId(parent.getReplyId()); //부모 튜플의 replyId를 reply의 parentId에 저장
        replyRepository.writeReReply(reply); //대댓글 작성

    }
}
