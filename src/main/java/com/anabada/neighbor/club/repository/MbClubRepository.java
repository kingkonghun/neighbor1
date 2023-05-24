package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.club.domain.Club;
import com.anabada.neighbor.used.domain.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MbClubRepository extends ClubRepository {
    @Override
    @Insert("")
    int clubPostInsert(Post post);

    @Override
    int clubInsert(Club club);

    @Override
    Long hobbySelect(String hobbyName);

    @Override
    @Select("select * from post")
    List<Post> clubPostList();

    @Override
    Club clubList(long postId);

    @Override
    String findHobbyName(long hobbyId);
}
