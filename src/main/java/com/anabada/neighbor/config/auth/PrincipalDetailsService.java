package com.anabada.neighbor.config.auth;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException { // 일반 로그인
        Member member = memberRepository.findByProviderId(memberEmail);
        if (member == null) {
            throw new UsernameNotFoundException("NOTHING");
        }
        return new PrincipalDetails(member);
    }
}
