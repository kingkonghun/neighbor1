package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.club.domain.PostSave;
import com.anabada.neighbor.club.domain.entity.Club;
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
    String findHobbyName(long hobbyId);
}
