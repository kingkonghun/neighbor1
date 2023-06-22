package com.anabada.neighbor.member.service;

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
   public List<Used> myWrite(PrincipalDetails principalDetails, Criteria criteria);//내가 쓴 글

    /**
     *페이징을 위한 내가 쓴 글 총 갯수 가져오기
     */
    public int getTotal(long principalDetails);//페이징

    /**
     * member 테이블에서 정보 가져오기
     */
   public Member myInfo(PrincipalDetails principalDetails);//내정보


   public String findProfileImg(long memberId);//프로필사진

   public void downProfileImg(HttpServletResponse response, String profileImg) throws IOException;

    /**
     * 내가 작성한 중고게시글 5개 .. 나중에 클럽도 포함 해야 할듯 함
     */
   public List<Used> myWriteFive(long memberId);//내가 작성한 글(중고게시글) 5개만


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
}
