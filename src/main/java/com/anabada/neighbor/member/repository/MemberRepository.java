package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository {

    void save(Member member);

    void saveOAuth(Member member);

    Member findByProviderId(String providerId);


    long findMemberId(String providerId);

    List<Post> findMyPost(long memberId);
}
