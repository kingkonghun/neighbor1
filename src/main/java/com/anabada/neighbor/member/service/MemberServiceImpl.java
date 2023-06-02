package com.anabada.neighbor.member.service;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.domain.Profile;
import com.anabada.neighbor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.List;

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

    @Override
    public Profile profile(HttpSession session) {//개인정보
        long memberId=(long)session.getAttribute("memberId");
        Member member = memberRepository.profile(memberId);

        return Profile.builder()
                .memberId(memberId)
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .address(member.getAddress())
                .addressDetail(member.getAddressDetail())
                .memberDate(member.getMemberDate())
                .profileImg(member.getProfileImg())
                .mbti(member.getMbti())
                .score(member.getScore())
                .memberStatus(member.getMemberStatus())
                .m(member.getMbti().charAt(0))
                .b(member.getMbti().charAt(1))
                .t(member.getMbti().charAt(2))
                .i(member.getMbti().charAt(3)).build();
    }





}
