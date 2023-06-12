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

}
