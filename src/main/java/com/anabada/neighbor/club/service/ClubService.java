package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.*;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.domain.entity.Hobby;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;
import com.anabada.neighbor.used.domain.Post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ClubService {
    /**
     * club 저장 후 성공 or 실패 반환
     *
     * @param club 클럽게시글정보
     * @return 성공하면1 실패하면0
     */
    public int saveClub(Club club);//clubSave

    /**
     * post 저장 후 postId 반환 실패하면 -1 반환
     * @param post 포스트게시글정보
     * @return 성공하면 postId 실패하면 -1 반환
     */
    public long savePost(Post post);//생성된 게시글의 postId 반환

    /**
     * 게시글 상세정보 조회
     * @param postId PK
     * @param principalDetails 현재 사용자 정보
     * @return 게시글 상세정보
     */
    public ClubResponse findClub(long postId, PrincipalDetails principalDetails);

    /**
     * clubRequest -> Post 로 변환
     * *** PostId 자동으로 들어가지않음 업데이트시 PostId 세팅 따로 해줘야함 ***
     * @param clubRequest 사용자가 보낸 게시글
     * @param principalDetails 사용자 정보
     * @return Post 객체
     */
    public Post clubRequestToPost(ClubRequest clubRequest, PrincipalDetails principalDetails);

    /**
     * clubRequest -> Post 로 변환
     * 게시글 수정이라 postId가 이미 있을때 사용
     * @param clubRequest clubRequest 사용자가 보낸 게시글
     * @param postId PK
     * @param principalDetails 사용자 정보
     * @return post 객체
     */
    public Post clubRequestToPost(ClubRequest clubRequest,Long postId, PrincipalDetails principalDetails);

    /**
     * clubRequest -> Club 으로 변환
     * @param clubRequest 사용자가 보낸 게시글
     * @param principalDetails 사용자 정보
     * @return Club 객체
     */
    public Club clubRequestToClub(ClubRequest clubRequest, PrincipalDetails principalDetails);


    public Message updatePost(Post post);

    public Message updateClub(Club clubRequest, ClubResponse clubResponse);

    public long deletePost(long postId);
    /**
     * 취미아이디 찾기
     * @param hobbyName 취미이름
     * @return hobbyId 취미아이디
     */
    public Long findHobbyId(String hobbyName);

    public List<ClubResponse> findClubList(int num, long hobbyId, String search, String listType, long postId);

    public int checkPost(ClubRequest clubRequest);//clubPost객체의 Null값체크

    /**
     * 모임가입 성공시 1 반환 인원이 꽉차서 실패시 -1반환
     * @param club 클럽정보
     * @param principalDetails 로그인한 사용자 정보
     * @return 성공시 1 인원이꽉차거나 실패시 -1 반환
     */
    int joinClubJoin(ClubResponse club, PrincipalDetails principalDetails);

    Long findClubJoinByMemberId(ClubResponse club, Long memberId);

    int deleteClubJoin(ClubResponse club, PrincipalDetails principalDetails);

    /**
     * 모임 현재인원 업데이트
     * @param num 1이면 증가 0이면 감소
     * @param clubId 클럽아이디
     * @return 성공시 1 실패시 0
     */
    void updateNowMan(int num, Long clubId);

    /**
     * 취미 이름 가져오기
     * @return
     */
    List<Hobby> findHobbyName();

    /**
     * 조회수 늘리기
     * @param postId 글ID
     *
     */
    void updatePostView(Long postId);

    /**
     *
     * @return 인덱스페이지에 뿌리는 동네모임 글
     */
    List<ClubResponse> mainList();
}
