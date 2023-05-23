package com.anabada.neighbor.used.service;

import com.anabada.neighbor.used.domain.*;
import com.anabada.neighbor.used.repository.UsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedServiceImpl implements UsedService{

    private final UsedRepository usedRepository;
    @Override
    public List<Used> list() {//글리스트
        List<Used> usedList = new ArrayList<>();
        List<Post> postList = usedRepository.postList();
        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            Product product = usedRepository.productList(post.getPostId());
            String categoryName = usedRepository.findCategoryName(product.getCategoryId());

            Used used = new Used(post.getTitle(), post.getContent(), post.getPostType(), post.getPostDate(),
                                    post.getPostUpdate(), post.getPostView(),
                                    product.getProductId(), categoryName, product.getPrice(), product.getProductStatus());
            usedList.add(used);
        }

        return usedList;

    }

    @Override
    public void write(Used used) {

    }

    @Override
    public void update(long postId) {

    }

    @Override
    public void delete(long postId) {

    }

    @Override
    public Used detail(long postId) {
        return null;
    }

    @Override
    public List<Img> images(long postId) {//사진
        return null;
    }
}
