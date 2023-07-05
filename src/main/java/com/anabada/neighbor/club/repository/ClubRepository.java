package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.domain.entity.Hobby;
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
     * @param fileRequest 이미지요청
     */
    public void insertFile(FileRequest fileRequest);

    /**
     * 업데이트 포스트
     *
     * @param post 업데이트한 포스트
     * @return 성공여부
     */
    public int updatePost(Post post);

    /**
     * 업데이트 클럽
     *
     * @param club 업데이트한 포스트
     * @return 성공여부
     */
    public int updateClub(Club club);

    public void deletePost(long postId);

    public Post selectPost(long postId);

    public Club selectClub(long postId);

    public Member selectMember(long memberId);

    public List<Post> selectPostList();

    /**
     * 이미지 리스트 조회
     * @param postId 게시글 번호 FK
     * @return 이미지 리스트
     */
    public List<FileResponse> selectImagesByPostId(Long postId);

    /**
     * 이미지 리스트 조회
     * @param imgId PK
     * @return 이미지 정보
     */
    public FileResponse selectImageByImgId(Long imgId);

    /**
     * 이미지 삭제
     * @param imgId PK
     */
    public void deleteImageByImgId(Long imgId);

    /**
     *
     * @param hobbyName 취미이름
     * @return hobbyId
     */
    public Long selectHobbyId(String hobbyName);

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

    /**
     * 클럽에 가입해있는지 찾기
     * @param clubId 클럽아이디
     * @param memberId 멤버아이디
     * @return clubJoinId 반환
     */
    Long selectClubJoinIdByMemberId(long clubId, long memberId);

    int insertClubJoin(Long clubId, Long memberId, Long postId);

    int deleteClubJoin(Long clubId, Long memberId);

    void updateNowManMinus(Long clubId);

    void updateNowManPlus(Long clubId);

    /**
     * 셀렉옵션에 넣을 취미 이름 가져오기
     * @return
     */
    List<Hobby> findHobbyName();

    List<Club> selectHobbyClubList(long hobbyId);

    List<Club> selectClubList();

    List<Post> selectHotPostList();

    String findMyClubLikePostType(long postId);

    /**
     * memberId로 like테이블에서 postId찾기
     * @param memberId
     * @return
     */
    List<Post> findPostId(long memberId);
}
