package com.anabada.neighbor.used.service;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.repository.UsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsedServiceImpl implements UsedService{

    private final UsedRepository usedRepository;
    List<MultipartFile> files= null;
    @Override
    public List<Used> list() {//글리스트
        List<Used> usedList = new ArrayList<>();
        List<Post> postList = usedRepository.postList();

        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            Member member = usedRepository.findMember(post.getMemberId());
            Product product = usedRepository.findProduct(post.getPostId());
            String categoryName = usedRepository.findCategoryName(product.getCategoryId());

            Used used = new Used(post.getPostId(), post.getTitle(), post.getContent(), post.getPostType(), post.getPostDate(),
                                    post.getPostUpdate(), post.getPostView(),
                                    product.getProductId(), categoryName, product.getPrice(), product.getProductStatus(), product.getCategoryId(),
                                    member.getMemberId(),member.getAddress(),member.getMemberName(),member.getProfileImg(),member.getScore(),member.getMemberStatus(),files
                    );
            usedList.add(used);
        }

        return usedList;

    }

    @Transactional
    @Override
    public void write(Used used) {//글쓰기

        usedRepository.writePost(used);
        usedRepository.writeProduct(used);

        try {
            String uploadDir = "C:\\upload_anabada";

            if (!Files.exists(Paths.get(uploadDir))) {
                Files.createDirectories(Paths.get(uploadDir));
            }

            for (MultipartFile file : used.getFiles()) {
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + "_" + file.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                file.transferTo(new File(filePath));
                usedRepository.writeImage(used.getPostId(),fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(long postId) {

    }

    @Override
    public void delete(long postId) {

    }

    @Override
    public Used detail(long postId) {
        Post post = usedRepository.findPost(postId);
        Product product = usedRepository.findProduct(postId);
        String categoryName = usedRepository.findCategoryName(product.getCategoryId());
        Member member = usedRepository.findMember(post.getMemberId());
        return new Used(post.getPostId(), post.getTitle(), post.getContent(), post.getPostType(), post.getPostDate(),
                post.getPostUpdate(), post.getPostView(),
                product.getProductId(), categoryName, product.getPrice(), product.getProductStatus(), product.getCategoryId(),
                member.getMemberId(),member.getAddress(),member.getMemberName(),member.getProfileImg(),member.getScore(),member.getMemberStatus(),files);
    }

    @Override
    public List<Img> images(long postId) {//사진
        return null;
    }
}
