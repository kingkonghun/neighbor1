package com.anabada.neighbor.member.service;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public void join(Member member) {
        memberRepository.join(member);
    }

    @Override
    public int login(Member member, HttpSession session) {
        int result = 0;
        Member member2 = memberRepository.login(member);
        if(member2 != null){
            session.setAttribute("memberId",member2.getMemberId());
            session.setAttribute("memberName",member2.getMemberName());
            result=1;
        }
        return result;
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public boolean passCheck(String memberEmail, String memberPassword) {
        return false;
    }

    @Override
    public void memberOut(HttpSession session) {

    }
}
