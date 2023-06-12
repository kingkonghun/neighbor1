package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface UsedRepository {
    public Product findProduct(long postId);

    public String findCategoryName(long categoryId);

    public Post findPost(long postId);

    public void writePost(Used used);
    public void writeProduct(Used used);
    public void writeImage(long postId,String imgUrl);


    public void update(Used used);
    public void updateProduct(Used used)throws Exception;
    public void updatePost(Used used)throws Exception;
    public void updateImage(long postId,String imgUrl)throws Exception;




    public void updatePostView(long postId);

    public Used detail(long postId);
    public String findImgUrl(long postId);

    public Member findMember(long memberId);

    public void deleteImg(long postId);

    public void deleteProduct(long postId);

    public void deletePost(long postId);

    public void deleteReply(long postId);

    public List<Category> categoryList();

    public List<Product> productList();

    public List<Product> productCategoryList(long categoryId);

    List<Post> postList();

    int findReplyCount(long postId);
}
