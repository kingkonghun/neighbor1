package com.anabada.neighbor.member.repository;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MbMemberRepository extends MemberRepository{

    @Override
    @Select("SELECT * FROM post" +
            " WHERE memberId=#{memberId} AND postType = 'used' " +
            " ORDER BY postId desc" +
            " LIMIT #{criteria.amount} OFFSET #{criteria.offset}")
    List<Post> findMyUsedWrite(Map<String,Object> map);

    @Override
    @Select("SELECT * FROM post" +
            " WHERE memberId=#{memberId} AND postType = 'club' " +
            " ORDER BY postId desc" +
            " LIMIT #{criteria.amount} OFFSET #{criteria.offset}")
    List<Post> findMyClubWrite(Map<String, Object> map);

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
    @Select("SELECT count(*) FROM post WHERE memberId=#{memberId} and postType='used'")
    int countMyUsedWrite(long memberId);

    @Override
    @Select("SELECT count(*) FROM post WHERE memberId=#{memberId} and postType='club'")
    int countMyClubWrite(long memberId);

    @Override
    @Select("SELECT count(*) FROM post WHERE memberId=#{memberId} and postType!='del'")
    int countMyAllWrite(long memberId);

    @Override
    @Select("SELECT * FROM post WHERE memberId=#{memberId} and postType='used' ORDER BY postUpdate DESC  LIMIT 5")
    List<Post> findMyPostFive(long memberId);


    @Override
    @Update("UPDATE member SET memberName=#{memberName},address=#{address}, addressDetail=#{addressDetail}, role='ROLE_USER' WHERE memberId=#{memberId}")
    void editInfo(Member member);



    @Override
    @Update("UPDATE member SET profileImg=#{profileImg} WHERE memberId=#{memberId}")
    void editProfileImg(Map<String, Object> map);

    @Override
    @Select("SELECT * FROM member ORDER BY memberId desc LIMIT #{criteria.amount} OFFSET #{criteria.offset}")
    List<Member> findAllMember(Map<String,Object> map);//관리자 모든 멤버 가져오기

    @Override
    @Select("SELECT memberName FROM member WHERE memberId=#{memberId}")
    String findMemberName(long memberId);//신고당한사람,신고자 이름가져오기

    @Override
    @Select("SELECT memberId,title FROM post WHERE postId=#{postId}")
    Post findReportedMember(long postId);

    @Override
    @Select("SELECT count(*) FROM likes WHERE memberId=#{memberId}")
    long countMyLikes(long memberId);//좋아요 누른 게시글 양 확인

    @Override
    @Select("select * from member where memberId = #{memberId}")
    Member findByMemberId(long reporterId);

    @Override
    @Select("SELECT count(*) FROM member")
    int countMember();

    @Override
    @Update("update member set score = #{reportedMemberScore} where memberId = #{memberId}")
    void updateScore(@Param("memberId") long memberId, @Param("reportedMemberScore") int reportedMemberScore);

    @Override
    @Select("SELECT memberPWD FROM member WHERE memberId=#{memberId}")
    String pwdCheck(long memberId);

    @Override
    @Update("UPDATE member SET memberPWD=#{memberPWD} WHERE memberId=#{memberId} ")
    void editPwd(@Param("memberPWD") String memberPWD, @Param("memberId") long memberId);

    @Override
    @Select("SELECT count(*) FROM member WHERE providerId = #{memberEmail}")
    int emailCheck(String memberEmail);
}

