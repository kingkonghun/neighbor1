package com.anabada.neighbor.member.service;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.domain.Profile;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface MemberService {
    public void join(Member member);//회원가입
    public int login(Member member, HttpSession session);//로그인
    public void logout(HttpSession session);//로그아웃
    public boolean passCheck(String memberEmail,String memberPassword);//비번확인
    public void memberOut(HttpSession session);

    Profile profile(HttpSession session);


}
