package com.anabada.neighbor.member.service;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.domain.Profile;

import javax.servlet.http.HttpSession;

public interface MemberService {

    public void save(Member member);

    Profile profile(HttpSession session);
}
