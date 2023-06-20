package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.used.domain.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface MbMemberRepository extends MemberRepository{

    @Override
    @Select("SELECT * FROM post" +
            " WHERE memberId=#{memberId}" +
            " ORDER BY postId desc" +
            " LIMIT #{criteria.amount} OFFSET #{criteria.offset}")
    List<Post> findMyPost(Map<String,Object> map);

    @Override
    @Select("SELECT count(*) FROM post WHERE memberId=#{memberId}")
    int getTotal(long memberId);

    @Override
    @Insert("insert into member (memberEmail, memberName, memberPWD, address, addressDetail, providerId, role) values (#{memberEmail}, #{memberName}, #{memberPWD}, #{address}, #{addressDetail}, #{providerId}, #{role})")
    void save(Member member);

    @Override
    @Insert("insert into member (memberEmail, memberName, memberPWD, providerId, role) values (#{memberEmail}, #{memberName}, #{memberPWD}, #{providerId}, #{role})")
    void saveOAuth(Member member);

    @Override
    @Select("select memberName, memberPWD, providerId, role from member where providerId = #{providerId}")
    Member findByProviderId(String providerId);


    @Override
    @Select("select memberId from member where providerId = #{providerId}")
    long findMemberId(String providerId);



    @Override
    @Select("SELECT * FROM member WHERE memberId=#{memberId}")
    Member findMyInfo(long memberId);//내정보

    @Override
    @Select("SELECT profileImg FROM member WHERE memberId=#{memberId}")
    String findProfileImg(long memberId);

    @Override
    @Select("SELECT count(*) FROM post WHERE memberId=#{memberId}")
    long countMyWrite(long memberId);

    @Override
    @Select("SELECT * FROM post WHERE memberId=#{memberId} ORDER BY postUpdate DESC  LIMIT 5")
    List<Post> findMyPostFive(long memberId);

    @Override
    @Update("UPDATE member SET memberName=#{memberName},memberPWD=#{memberPWD},address=#{address}, addressDetail=#{addressDetail} WHERE memberId=#{memberId}")
    void editInfo(Member member);

    @Override
    @Update("UPDATE member SET memberName=#{memberName},address=#{address}, addressDetail=#{addressDetail} WHERE memberId=#{memberId}")
    void editInfoNotPwd(Member member);

    @Override
    @Update("UPDATE member SET profileImg=#{profileImg} WHERE memberId=#{memberId}")
    void editProfileImg(Map<String, Object> map);

    @Override
    @Select("SELECT * FROM member ORDER BY memberId desc LIMIT #{criteria.amount} OFFSET #{criteria.offset} ")
    List<Member> findAllMember(Map<String,Object> map);//관리자 모든 멤버 가져오기

    @Override
    @Select("SELECT memberName FROM member WHERE memberId=#{memberId}")
    String findMemberName(long memberId);//신고당한사람,신고자 이름가져오기

    @Override
    @Select("SELECT memberId,title FROM post WHERE postId=#{postId}")
    Post findReportedMember(long postId);

    @Override
    @Select("SELECT count(*) FROM likes WHERE memberId=#{memberId}")
    long countMyLikes(long memberId);
}

