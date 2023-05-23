package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {
    Member login(Member member);

    void join(Member member);
}
