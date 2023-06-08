package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MbMemberRepository extends MemberRepository{

    @Override
    @Insert("insert into member (memberEmail, memberName, memberPWD, address, addressDetail, mbti, role) values (#{memberEmail}, #{memberName}, #{memberPWD}, #{address}, #{addressDetail}, #{mbti}, #{role})")
    void save(Member member);

    @Override
    @Insert("insert into member (memberEmail, memberName, memberPWD, providerId, role) values (#{memberEmail}, #{memberName}, #{memberPWD}, #{providerId}, #{role})")
    void saveOAuth(Member member);

    @Override
    @Select("select * from member where memberEmail = #{memberEmail}")
    Member findByMemberEmail(String memberEmail);

    @Override
    @Select("SELECT * FROM member WHERE memberId = #{memberId}")
    Member profile(long memberId);
}

