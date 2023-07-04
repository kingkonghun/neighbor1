package com.anabada.neighbor.member.service;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.used.domain.Used;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface MemberService {

    /**
     * 회원 가입
     */
    public void save(Member member);


    /**
     *내가 작성한 게시글 리스트 (Criteria 로 페이징)
     */
   public List<Used> myUsedWrite(PrincipalDetails principalDetails, Criteria criteria);//내가 쓴 글

    /**
     *페이징을 위한 내가 쓴 글 총 갯수 가져오기
     */
    public int getUsedTotal(long memberId);//페이징

    /**
     * member 테이블에서 정보 가져오기
     */
   public Member myInfo(long memberId);//내정보


   public String findProfileImg(long memberId);//프로필사진

   public void downProfileImg(HttpServletResponse response, String profileImg) throws IOException;//프로필사진 헤더에보내기 (다운)


    /**
     * member테이블
     * 개인정보 수정
     */
   public void editInfo(Member member);//개인정보 수정

    /**
     *관리자페이지에서 모든 member 가져오기
     */
    List<Member> findAllMember(Criteria criteria);//관리자 모든멤버 가져오기

    /**
     * 페이징을 위한 멤버의 총 수 가져오기
     * @return 멤버테이블의 count
     */
    int countMember();

    /**
     * 비밀번호수정
     * @param oldPwd
     * @param memberPWD
     */
    String editPwd(String oldPwd, String memberPWD,long memberId);


    void editPhoto(Member member);//사진 수정 아마 삭제


    /**
     *
     * @param principalDetails 내 멤버아이디
     * @param criteria 페이징처리
     * @return 내가 쓴 동네모임 글 목록
     */
    List<ClubResponse> myClubWrite(PrincipalDetails principalDetails, Criteria criteria);

    /**
     *
     * @param memberId 내 멤버아이디
     * @return 페이징을 위한 동네모임글 총 수
     */
    int getClubTotal(long memberId);



    /**
     * 이메일 중복체크
     * @param memberEmail
     * @return
     */
    boolean emailCk(String memberEmail);
}
