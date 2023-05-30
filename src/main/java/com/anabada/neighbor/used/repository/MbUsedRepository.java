package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MbUsedRepository extends UsedRepository {
    @Select("select * from post")
    public List<Post> postList();
    @Select("select * from product where postId = #{postId}")
    public Product findProduct(long postId);
    @Select("select categoryName from category where categoryId = #{categoryId}")
    public String findCategoryName(long categoryId);

    @Select("SELECT*FROM member WHERE memberId=#{memberId}")
    public Member findMember(long memberId);

    @Select("select * from post where postId = #{postId}")
    public Post findPost(long postId);

    @Insert("INSERT INTO post (memberId,title,content,postType)" +
            "VALUES(#{memberId},#{title},#{content},'used')")
    @Options(useGeneratedKeys = true, keyProperty = "postId")
    public void writePost(Used used);

    @Insert("insert into product (postId, categoryId, price) values (#{postId},#{categoryId},#{price})")
    public void writeProduct(Used Used);

    @Insert("INSERT INTO img (postId,imgUrl) VALUES(#{postId},#{imgUrl})")
    public void writeImage(@Param("postId") long postId, @Param("imgUrl") String imgUrl);


    @Update("UPDATE  product SET categoryId=#{categoryId},price=#{price} WHERE postId=46")
    public void updateProduct(Used used);

    @Update("UPDATE  post SET title=#{title},content=#{content},postUpdate=now() WHERE postId=#{postId}")
    public void updatePost(Used used);
    @Update("UPDATE img SET imgUrl=#{imgUrl} WHERE postId=#{postId}")
    public void updateImage(long postId,String imgUrl);

    public void delete(long postId);
    public Used detail(long postId);
    @Select("SELECT imgUrl FROM img WHERE postId=#{postId} ORDER BY imgId LIMIT 1")
    public String findImgUrl(long postId);

}
