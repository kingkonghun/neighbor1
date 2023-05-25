package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.multipart.MultipartFile;

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
    public void writeImage(String imgUrl,long postId);
    public void update(long postId);
    public void delete(long postId);
    public Used detail(long postId);
    public List<Img> images(long postId);
}
