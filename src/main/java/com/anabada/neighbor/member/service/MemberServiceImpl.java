package com.anabada.neighbor.member.service;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.domain.Profile;
import com.anabada.neighbor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(Member member) {
        member.setMemberPWD(bCryptPasswordEncoder.encode(member.getMemberPWD()));
        member.setRole("ROLE_USER");
        memberRepository.save(member);
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
