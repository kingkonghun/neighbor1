package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsedRepository {
    public List<Post> postList();
    public Product productList(long postId);

    public String findCategoryName(long categoryId);

    public void insertPost(Post post);
    public void insertProduct(Product product);

    public void update(long postId);
    public void delete(long postId);
    public Used detail(long postId);
    public List<Img> images(long postId);
}
