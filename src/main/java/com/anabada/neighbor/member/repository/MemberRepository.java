package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Used;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Repository
public interface MemberRepository {

    void save(Member member);

    void saveOAuth(Member member);

    Member findByProviderId(String providerId);


    long findMemberId(String providerId);

    List<Post> findMyPost(Map<String,Object> map);//내가 쓴 글

    public int getTotal(long memberId);//페이징

    Member findMyInfo(long memberId);//내 정보

    String findProfileImg(long memberId);//프로필사진url찾기

    long countMyWrite(long memberId);

    List<Post> findMyPostFive(long memberId);//내가작성한글5개만

    void editInfo(Member member);//비밀번호가 들어왔을경우

    void editInfoNotPwd(Member member);//비밀번호 안들어왔을때

    void editProfileImg(Map<String, Object> map);

    List<Member> findAllMember(Map<String,Object> map);

    String findMemberName(long memberId);

    Post findReportedMember(long postId);

    long countMyLikes(long memberId);

    Member findByMemberId(long reporterId);
}
