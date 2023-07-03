package com.anabada.neighbor.config.oauth;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.config.oauth.provider.GoogleUserInfo;
import com.anabada.neighbor.config.oauth.provider.KakaoUserInfo;
import com.anabada.neighbor.config.oauth.provider.NaverUserInfo;
import com.anabada.neighbor.config.oauth.provider.OAuth2UserInfo;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // 소셜 로그인

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        Member member = memberRepository.findByProviderId(oAuth2UserInfo.getProviderId());
        if (member == null) {
            member = Member.builder()
                    .memberEmail(oAuth2UserInfo.getEmail())
                    .memberName(oAuth2UserInfo.getName())
                    .memberPWD(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                    .providerId(oAuth2UserInfo.getProviderId())
                    .role("ROLE_GUEST")
                    .build();
            memberRepository.saveOAuth(member);
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
