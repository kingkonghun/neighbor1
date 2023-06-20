package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UsedRepository {
    /**
     * product 테이블을 postId로 조회
     */
    public Product findProduct(long postId);

    /**
     * category 테이블의 categoryName 을 categoryId 로 조회
     */
    public String findCategoryName(long categoryId);

    /**
     * post 테이블을 postId로 조회
     */
    public Post findPost(long postId);

    /**
     * post 테이블 insert
     */
    public void writePost(Used used);

    /**
     * product 테이블 insert
     */
    public void writeProduct(Used used);

    /**
     * img 테이블 insert
     */
    public void writeImage(long postId,String imgUrl);

    /**
     * product 테이블 update
     */
    public void updateProduct(Used used)throws Exception;

    /**
     * post 테이블 update
     */
    public void updatePost(Used used)throws Exception;

    /**
     * img 테이블 update
     */
    public void updateImage(long postId,String imgUrl)throws Exception;

    /**
     * post 테이블 postView(조회수) 증가
     */
    public void updatePostView(long postId);

    /**
     * 삭제예정
     */
    public String findImgUrl(long postId);

    /**
     * 삭제예정
     */
    public Member findMember(long memberId);

    /**
     * 삭제예정
     */
    public void deleteImg(long postId);

    /**
     * 수정예정
     */
    public void deleteProduct(long postId);

    /**
     * 수정예정
     */
    public void deletePost(long postId);

    /**
     * 수정예정
     */
    public void deleteReply(long postId);

    /**
     * category 테이블 전부 조회
     */
    public List<Category> categoryList();

    /**
     * product 테이블 전부 조회
     */
    public List<Product> productList();

    /**
     * product 테이블을 categoryId로 조회
     */
    public List<Product> productCategoryList(long categoryId);

    /**
     * post 테이블 전부 조회
     */
    List<Post> postList();

    /**
     * postId 로 댓글 갯수 조회
     */
    int findReplyCount(long postId);

    /**
     * postId 로 좋아요 갯수 조회
     */
    int findLikesCount(long postId);

    /**
     * 좋아요 up
     */
    void likesUp(Likes likes);

    /**
     * 로그인 중인 사용자가 해당 게시물에 좋아요를 눌렀는지 확인
     */
    int likesCheck(Likes likes);

    /**
     * 좋아요 down
     */
    void likesDown(Likes likes);

    /**
     * reportType 전부 조회
     */
    List<ReportType> findAllReportType();

    /**
     * report 테이블 insert(게시물 신고)
     */
    void report(Report report);

    /**
     * 신고내역 리스트 조회(map 은 페이징 처리 하기 위함)
     */
    List<Report> findAllReport(Map<String, Object> map);

    /**
     * reportType 테이블에서 reportTypeId 로 reportTypeName 조회
     */
    String findReportTypeName(long reportTypeId);

    /**
     * memberId 로 좋아요 누른 게시물 정보 조회
     */
    List<Likes> findLikePosts(long memberId);
}
