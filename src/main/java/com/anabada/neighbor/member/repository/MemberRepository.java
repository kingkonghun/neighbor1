package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.domain.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository {
    Member login(Member member);

    void join(Member member);

    Member profile(long memberId);//개인정보
}
