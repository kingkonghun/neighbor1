package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Category;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MbUsedRepository extends UsedRepository {

    @Override
    @Select("SELECT *" +
            " FROM post" +
            " WHERE postUpdate >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 week)" +
            " ORDER BY postView desc" +
            " LIMIT 8")
    List<Post> postList();

    @Select("SELECT*FROM category")
    List<Category> categoryList();
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

    public Used detail(long postId);
    @Select("SELECT imgUrl FROM img WHERE postId=#{postId} ORDER BY imgId LIMIT 1")
    public String findImgUrl(long postId);

    @Delete("DELETE FROM reply WHERE postId=#{postId}")
    public void deleteReply(long postId);
    @Delete("DELETE FROM img WHERE postId=#{postId}")
    public void deleteImg(long postId);
    @Delete("DELETE FROM product WHERE postId=#{postId}")
    public void deleteProduct(long postId);
    @Delete("DELETE FROM post WHERE postId=#{postId}")
    public void deletePost(long postId);

    @Override
    @Update("update post set postView = postView + 1 where postId = #{postId}")
    public void updatePostView(long postId);

    @Override
    @Select("select * from product")
    public List<Product> productList();

    @Override
    @Select("select * from product where categoryId = #{categoryId}")
    public List<Product> productCategoryList(long categoryId);
}
