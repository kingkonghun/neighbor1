package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MbUsedRepository extends UsedRepository {
    public List<Post> postList();
    public List<Product> productList(long postId);
    public String findCategoryName(long categoryId);
    public void write();
    public void update(long postId);
    public void delete(long postId);
    public Used detail(long postId);
    public List<Img> images(long postId);
}
