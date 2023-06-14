package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository {
    /**
     * 포스트 저장
     * @param post 포스트
     * @return 성공하면 1 실패하면 0
     */
    public int insertPost(Post post);

    public int insertClub(Club club);

    /**
     * 요청받은 이미지 정보 db에 넣기
     * @param imageRequest 이미지요청
     */
    public void insertImage(ImageRequest imageRequest);

    /**
     * 업데이트 포스트
     * @param post 업데이트한 포스트
     */
    public void updatePost(Post post);

    public void updateClub(Club club);

    public void deletePost(long postId);

    public Post selectPost(long postId);

    public Club selectClub(long postId);

    public Member selectMember(long memberId);

    public List<Post> selectPostList();
    /**
     *
     * @param hobbyName 취미이름
     * @return hobbyId
     */
    public long selectHobbyId(String hobbyName);

    /**
     *
     * @param hobbyId 취미아이디
     * @return hobbyName
     */
    public String selectHobbyName(long hobbyId);

    /**
     *
     * @param memberId 멤버아이디
     * @return memberName
     */
    public String selectMemberName(long memberId);

    /**
     * 전체 게시글 수를 조회하는 select 쿼리 페이징기능에 사용
     * @return 전체 게시글 수
     */
    int count();

}
