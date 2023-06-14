package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MbClubRepository extends ClubRepository {

    //여기서 post setter 바꿔도 postId 가져올 수 있는지 확인
    @Override
    @Insert("insert into post(memberId, title, content, postType) " +
            "values(#{memberId}, #{title}, #{content}, #{postType})")
    @Options(useGeneratedKeys = true, keyProperty = "postId")//자동으로 pk값을 가져오기 위한옵션
    int insertPost(Post post);

    @Override
    @Insert("insert into club(postId, memberId, hobbyId, maxMan)" +
            "values(#{postId}, #{memberId}, #{hobbyId}, #{maxMan})")
    int insertClub(Club club);

    @Override
    @Insert("insert into file(" +
            "imgId, postId, origName, saveName, size, deleteYn, creaDate, deleDate)" +
            "values(#{imgId}, #{postId}, #{origName}, #{saveName}, #{size}, 0, now(), null)")
    void insertImage(ImageRequest imageRequest);

    @Override
    @Update("update post" +
            " set title = #{title}, content=#{content}, postUpdate= now()" +
            " where postId = #{postId}")
    void updatePost(Post post);

    @Override
    @Update("update club" +
            " set hobbyId = #{hobbyId}, maxMan = #{maxMan}")
    void updateClub(Club club);

    @Override
    @Delete("update post" +
            " set postType = 'del' " +
            "where postId = #{postId}")
    void deletePost(long postId);

    @Override
    @Select("select * from post where postId = #{postId}")
    Post selectPost(long postId);

    @Override
    @Select("select * from club where postId = #{postId}")
    Club selectClub(long postId);

    @Override
    @Select("select * from member where memberId = #{memberId}")
    Member selectMember(long memberId);

    @Override
    @Select("select * from post where postType = 'club' order by postId desc")//6개리스트만가져오기
    List<Post> selectPostList();

    @Override
    @Select("select hobbyId from hobby where hobbyName = #{hobbyName}")
    long selectHobbyId(String hobbyName);

    @Override
    @Select("select hobbyName from hobby where hobbyId = #{hobbyId}")
    String selectHobbyName(long hobbyId);

    @Override
    @Select("select memberName from member where memberId = #{memberId}")
    String selectMemberName(long memberId);

    @Override
    int count();
}
