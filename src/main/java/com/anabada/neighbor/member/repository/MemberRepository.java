package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {

    void save(Member member);

    void saveOAuth(Member member);

    Member findByMemberEmail(String memberEmail);

    Member profile(long memberId);//개인정보

}
