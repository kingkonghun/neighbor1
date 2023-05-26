package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.club.domain.PostSave;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MbClubRepository extends ClubRepository {

    @Override
    @Insert("insert into post(memberId, title, content, postType) " +
            "values(#{memberId}, #{title}, #{content}, #{postType})")
    @Options(useGeneratedKeys = true, keyProperty = "postId")//자동으로 pk값을 가져오기 위한옵션
    int clubPostSave(PostSave postSave);

    @Override
    @Insert("insert into club(postId, memberId, hobbyId, maxMan)" +
            "values(#{postId}, #{memberId}, #{hobbyId}, #{maxMan})")
    int clubSave(Club club);

    @Override
    @Select("select hobbyId from hobby where hobbyName = #{hobbyName}")
    long findByHobbyId(String hobbyName);

    @Override
    @Select("select hobbyName from hobby where hobbyId = #{hobbyId}")
    String findHobbyName(long hobbyId);

    @Override
    @Select("select * from post where postType = 'club' order by postId desc limit 6")//6개리스트만가져오기
    List<Post> clubPostList();

    @Override
    @Select("select memberName from member where memberId = #{memberId}")
    String findByMemberName(long memberId);

    @Override
    @Select("select * from club where postId = #{postId}")
    Club findByClubOne(long postId);

    @Override
    @Select("select * from member where memberId = #{memberId}")
    Member findByMemberOne(long memberId);
}
