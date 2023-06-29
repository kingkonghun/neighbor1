package com.anabada.neighbor.used.repository;


import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MbUsedRepository extends UsedRepository {

    @Override
    @Select("SELECT *" +
            " FROM post" +
            " WHERE postUpdate >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 week) and postType='used' " +
            " ORDER BY postView desc" +
            " LIMIT 6")
    List<Post> postList();

    @Override
    @Select("SELECT * FROM category")
    List<Category> categoryList();

    @Override
    @Select("select * from product where postId = #{postId} and (productStatus = 'y' or productStatus = 'n')")
    public Product findProduct(long postId);

    @Override
    @Select("select categoryName from category where categoryId = #{categoryId}")
    public String findCategoryName(long categoryId);

    @Override
    @Select("SELECT*FROM member WHERE memberId=#{memberId}")
    public Member findMember(long memberId);

    @Override
    @Select("select * from post where postId = #{postId} and postType = 'used'")
    public Post findPost(long postId);

    @Override
    @Insert("INSERT INTO post (memberId,title,content,postType)" +
            "VALUES(#{memberId},#{title},#{content},'used')")
    @Options(useGeneratedKeys = true, keyProperty = "postId")
    public void writePost(Used used);

    @Override
    @Insert("insert into product (postId, categoryId, price) values (#{postId},#{categoryId},#{price})")
    public void writeProduct(Used Used);

    @Override
    @Insert("INSERT INTO img (postId,imgUrl) VALUES(#{postId},#{imgUrl})")
    public void writeImage(@Param("postId") long postId, @Param("imgUrl") String imgUrl);

    @Override
    @Update("UPDATE  product SET categoryId=#{categoryId},price=#{price} WHERE postId=46")
    public void updateProduct(Used used);

    @Override
    @Update("UPDATE  post SET title=#{title},content=#{content},postUpdate=now() WHERE postId=#{postId}")
    public void updatePost(Used used);

    @Override
    @Update("UPDATE img SET imgUrl=#{imgUrl} WHERE postId=#{postId}")
    public void updateImage(long postId,String imgUrl);

    @Override
    @Select("SELECT imgUrl FROM img WHERE postId=#{postId} ORDER BY imgId LIMIT 1")
    public String findImgUrl(long postId);

    @Override
    @Delete("DELETE FROM reply WHERE postId=#{postId}")
    public void deleteReply(long postId);

    @Override
    @Delete("DELETE FROM img WHERE postId=#{postId}")
    public void deleteImg(long postId);

    @Override
    @Update("update product set productStatus = 'del' where postId = #{postId}")
    public void deleteProduct(long postId);

    @Override
    @Update("update post set postType = 'del' where postId = #{postId}")
    public void deletePost(long postId);

    @Override
    @Update("update post set postView = postView + 1 where postId = #{postId}")
    public void updatePostView(long postId);

    @Override
    @Select("select * from product where productStatus = 'y' or productStatus = 'n'")
    public List<Product> productList();

    @Override
    @Select("select * from product where categoryId = #{categoryId} and productStatus = 'y'")
    public List<Product> productCategoryList(long categoryId);

    @Override
    @Select("select count(*) from reply where postId = #{postId}")
    int findReplyCount(long postId);

    @Override
    @Select("SELECT * FROM likes WHERE memberId=#{memberId} ORDER BY likeId desc LIMIT #{criteria.amount} OFFSET #{criteria.offset}")
    List<Likes> findLikePosts(Map<String, Object> map);

    @Override
    @Select("select count(*) from likes where postId = #{postId}")
    int findLikesCount(long postId);

    @Override
    @Select("select count(*) from likes where postId = #{postId} and memberId = #{memberId}")
    int likesCheck(Likes likes);

    @Override
    @Insert("insert into likes (postId, memberId) values (#{postId}, #{memberId})")
    void likesUp(Likes likes);

    @Override
    @Delete("delete from likes where postId = #{postId} and memberId = #{memberId}")
    void likesDown(Likes likes);

    @Override
    @Select("select * from reportType")
    List<ReportType> findAllReportType();

    @Override
    @Insert("insert into report (postId, reporterId, content, reportTypeId) values (#{postId}, #{reporterId}, #{content}, #{reportTypeId})")
    void report(Report report);

    @Override
    @Select("SELECT * FROM report ORDER BY reportId desc LIMIT #{criteria.amount} OFFSET #{criteria.offset} ")
    List<Report> findAllReport(Map<String, Object> map);

    @Override
    @Select("SELECT reportTypeName FROM reportType WHERE reportTypeId=#{reportTypeId}")
    String findReportTypeName(long reportTypeId);

    @Override
    @Select("SELECT count(*) FROM report")
    int countReport();

    @Override
    @Select("select * from report where reportId = #{reportId}")
    Report findByReportId(long reportId);

    @Override
    @Select("select memberId from post where postId = #{postId}")
    long findMemberId(long postId);

    @Override
    @Update("update report set reportStatus = 'n' where reportId = #{reportId}")
    void UpdateReportStatus(long reportId);

    @Override
    @Select("SELECT imgUrl FROM img WHERE postId=#{postId}")
    List<String> findAllImgUrl(long postId);

    @Override
    @Update("UPDATE product SET productStatus = 'n' WHERE postId=#{postId}")
    void soldOut(long postId);

    @Override
    @Insert("insert into sales (postId, memberId) values (#{postId}, #{memberId})")
    void insertSales(@Param("postId") long postId, @Param("memberId") long memberId);

    @Override
    @Insert("insert into purchase (postId, memberId) values (#{postId}, #{memberId})")
    void insertPurchase(@Param("postId") long postId, @Param("memberId") long receiver);

    @Override
    @Select("select * from purchase where memberId = #{memberId}")
    List<Purchase> findPurchaseByMemberId(long memberId);

    @Override
    @Select("select * from sales where memberId = #{memberId}")
    List<Sales> findSalesByMemberId(long memberId);
}
