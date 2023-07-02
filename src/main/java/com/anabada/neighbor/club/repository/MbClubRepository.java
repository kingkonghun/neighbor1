package com.anabada.neighbor.club.repository;

import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.ImageResponse;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.domain.entity.Hobby;
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
    @Select("select * from file where deleteYn = 0 and postId = #{postId} order by imgId")
    List<ImageResponse> selectImagesByPostId(Long postId);

    @Override
    @Select("select * from file" +
            " where deleteYn = 0 and imgId = #{imgId} order by imgId")
    ImageResponse selectImageByImgId(Long imgId);

    @Override
    @Delete("update file set deleteYn = 1, deleDate = NOW()" +
            " where imgId IN (#{imgId})")
    void deleteImageByImgId(Long imgId);

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
    @Insert("insert into clubJoin (clubId, memberId, postId) values (#{clubId}, #{memberId}, #{postId})")
    int insertClubJoin(@Param("clubId") Long clubId, @Param("memberId") Long memberId,@Param("postId") Long postId);

    @Override
    @Delete("delete from clubJoin where clubId = #{clubId} and memberId = #{memberId}")
    int deleteClubJoin(@Param("clubId") Long clubId, @Param("memberId") Long memberId);

    @Override
    @Select("select id from clubJoin where clubId = #{clubId} and memberId = #{memberId} ")
    Long selectClubJoinIdByMemberId(@Param("clubId") long clubId,@Param("memberId") long memberId);

    @Override
    @Update("update club set nowMan = nowMan - 1 where clubId = ${clubId}")
    void updateNowManMinus(Long clubId);

    @Override
    @Update("update club set nowMan = nowMan + 1 where clubId = ${clubId}")
    void updateNowManPlus(Long clubId);

    @Override
    int count();

    @Override
    @Select("select * from hobby")
    List<Hobby> findHobbyName();
}
