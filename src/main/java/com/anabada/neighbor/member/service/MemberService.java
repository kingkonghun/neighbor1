package com.anabada.neighbor.member.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.used.domain.Used;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface MemberService {

    public void save(Member member);


    List<Used> myWrite(PrincipalDetails principalDetails, Criteria criteria);//내가 쓴 글
    public int getTotal(PrincipalDetails principalDetails);//페이징

    Member myInfo(PrincipalDetails principalDetails);//내정보

    String findProfileImg(long memberId);//프로필사진

    void downProfileImg(HttpServletResponse response, String profileImg) throws IOException;
}
