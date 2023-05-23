package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MbMemberRepository extends MemberRepository{

    @Select("SELECT memberId,memberName FROM member WHERE memberEmail=#{memberEmail} and memberPWD=#{memberPWD} and memberStatus='y'")
    Member login(Member member);

    @Insert("INSERT INTO member(memberName,memberPWD,memberEmail,address,addressDetail,mbti)" +
            "VALUES(#{memberName},#{memberPWD},#{memberEmail},#{address},#{addressDetail},#{mbti})")
    void join(Member member);
}
