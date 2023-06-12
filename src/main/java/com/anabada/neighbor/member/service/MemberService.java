package com.anabada.neighbor.member.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.domain.Profile;
import com.anabada.neighbor.used.domain.Used;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface MemberService {

    public void save(Member member);


    List<Used> myWrite(PrincipalDetails principalDetails);
}
