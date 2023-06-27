package com.anabada.neighbor.reply.repository;

import com.anabada.neighbor.reply.domain.Reply;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReplyRepository {
    /**
     * reply 테이블에서 postId 로 댓글 조회
     */
    public List<Reply> list(long postId);

    /**
     * member 테이블의 memberName 을 memberId 로 조회
     */
    public String findMemberName(long memberId);

    /**
     * reply 테이블 insert(댓글 작성)
     */
    public void write(Reply reply);

    /**
     * reply 테이블의 reGroup 을 replyId 로 업데이트(댓글 그룹 형성)
     */
    public void updateReGroup(Reply reply);

    /**
     * 댓글 삭제(comment 컬럼을 빈 값으로 update)
     */
    public void delete(long replyId);

    /**
     * reply 테이블 update
     */
    public void update(Reply reply);

    /**
     * reply 테이블 insert(대댓글 작성)
     */
    public void writeReReply(Reply reply);

    /**
     * replyId 로 reply 테이블 조회
     */
    public Reply findReply(long replyId);

    /**
     * memberId 로 reply 테이블 조회(내가 쓴 댓글 조회)
     */
    List<Reply> findMyReply(Map<String,Object> map);

    int countMyReply(long memberId);
}
