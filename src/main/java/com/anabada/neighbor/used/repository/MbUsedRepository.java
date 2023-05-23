package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MbUsedRepository extends UsedRepository {
    @Select("select * from post where postId=1")
    public List<Post> postList();
    @Select("select * from product where postId = #{postId}")
    public Product productList(long postId);
    @Select("select categoryName from category where categoryId = #{categoryId}")
    public String findCategoryName(long categoryId);

    public void insertPost(Post post);
    public void insertProduct(Product product);

    public void update(long postId);
    public void delete(long postId);
    public Used detail(long postId);
    public List<Img> images(long postId);
}
