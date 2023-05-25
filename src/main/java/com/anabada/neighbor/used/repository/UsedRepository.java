package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface UsedRepository {
    public List<Post> postList();
    public Product findProduct(long postId);

    public String findCategoryName(long categoryId);

    public Post findPost(long postId);

    public void writePost(Used used);
    public void writeProduct(Used used);

    public void update(long postId);
    public void delete(long postId);
    public Used detail(long postId);
    public List<Img> images(long postId);

    public Member findMember(long memberId);

    public void writeImage(long postId,String imgUrl);
}
