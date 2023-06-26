package com.anabada.neighbor.reply.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;

import java.util.List;

public interface ReplyService {
    /**
     * postId 로 댓글 리스트 가져오기
     */
    public List<CarryReply> list(long postId);

    /**
     * 댓글 작성
     */
    public void write(Reply reply, PrincipalDetails principalDetails);

    /**
     * 댓글 삭제(update 로 comment 컬럼 빈값으로 변경)
     */
    public void delete(long replyId);

    /**
     * 댓글 수정
     */
    public void update(Reply reply);

    /**
     * 대댓글 작성
     */
    public void writeReReply(Reply reply, PrincipalDetails principalDetails);

    /**
     * 내가 쓴 댓글 리스트 조회
     */
    List<CarryReply> findMyReply(long memberId);
}
