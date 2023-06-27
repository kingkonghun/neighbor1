package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MemberRepository {

    /**
     * 회원 가입
     */
    void save(Member member);

    /**
     *  소셜 로그인시 소셜의 정보를 디비에 저장
     */
    void saveOAuth(Member member);

    /**
     * 로그인시
     * 사이트 회원인지, 소셜로그인 회원인지 구분..
     */
    Member findByProviderId(String providerId);

    /**
     * providerId 로 멤버 아이디 찾기
     */
    long findMemberId(String providerId);

    /**
     * 내가 작성한 글 List
     * 페이징 처리를 위해 memberId와 Criteria 객체를 map에 넣음
     */
    List<Post> findMyPost(Map<String,Object> map);//내가 쓴 글



    /**
     * memberId로 내 정보를 가져옴
     */
    Member findMyInfo(long memberId);//내 정보

    String findProfileImg(long memberId);//프로필사진url찾기


    /**
     * 내가 작성한 글 총 갯수 확인
     */
    int countMyWrite(long memberId);

    /**
     * 내가 작성한 게시글 5개
     */
    List<Post> findMyPostFive(long memberId);//내가작성한글5개만

    /**
     * 개인 정보 수정
     */
    void editInfo(Member member);//비밀번호가 들어왔을경우



    /**
     * 프로필이미지 수정
     * memberId와 imgUrl을 map에 넣음
     */
    void editProfileImg(Map<String, Object> map);

    /**
     * 관리자 페이지에서 확인하는 모든 멤버리스트
     * 페이징처리를 위해 Criteria 객체를 map 에 넣음
     */
    List<Member> findAllMember(Map<String,Object> map);

    /**
     * 신고 당한 사람의 닉네임을 memberId 로 가져옴
     */
    String findMemberName(long memberId);

    /**
     * 신고 당한 게시글의
     * postId로 memberId, title 을 가져옴
     */
    Post findReportedMember(long postId);

    /**
     * memberId로 좋아요 누른 게시글의 갯수 확인
     */
    long countMyLikes(long memberId);

    /**
     *  신고한 사람의 id로
     *  정보를 가져옴
     */
    Member findByMemberId(long reporterId);

    /**
     * 페이징을 위한 멤버의 총 수 가져오기
     * @return 멤버테이블의 count
     */
    int countMember();

    void updateScore(long memberId, int reportedMemberScore);

    /**
     * 비밀번호 수정전에
     * @param  기존 비밀번호가
     *  일치하는지 확인
     */
    String pwdCheck(long memberId);

    /**
     * 비밀번호 변경
     * @param memberPWD
     * @param memberId
     */
    void editPwd(String memberPWD, long memberId);
}
