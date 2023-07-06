package com.anabada.neighbor.used.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.used.domain.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UsedService {
    /**
     * 게시물 타입이 used 인 list 조회
     * */
    public List<Used> list(long categoryId, String listType, int num, String search, long postId);
    /**
     * 게시물 작성
     * */
    public void write(Used used, PrincipalDetails principalDetails)throws Exception;
    /**
     * 게시물 업데이트
     * */
    public void update(Used used, PrincipalDetails principalDetails) throws Exception;
    /**
     * 게시물 삭제
     * */
    public void delete(long postId);
    /**
     * 게시물 상세보기
     * */
    public Used detail(long postId, HttpServletRequest request, HttpServletResponse response, PrincipalDetails principalDetails);

    /**
     * 카테고리 리스트
     */
    List<Category> categoryList();

    /**
     * 메인페이지 중고상품 리스트
     */
    List<Used> mainList();

    /**
     * 게시물 좋아요 업, 다운
     */
    Used likes(long postId, PrincipalDetails principalDetails, int likesCheck);

    /**
     * 신고타입 리스트
     */
    List<ReportType> reportType();

    /**
     * 게시물 신고
     */
    void report(Report report, PrincipalDetails principalDetails);

    /**
     * 신고내역 리스트
     */
    List<PostReport> findAllReport(Criteria criteria);

    /**
     * memberId 로 좋아요 누른 게시물
     */
    List<Used> likePost(long memberId,Criteria criteria);

    /**
     * 페이징을 위한 신고된 게시글의 총 수
     * */
    int countReport();

    void reportOk(ReportOk reportOk);

    /**
     * 판매완료
     * @param postId
     */
    void soldOut(long postId, long receiver, PrincipalDetails principalDetails);

    /**
     * 구매목록
     * @param principalDetails
     * @param criteria 페이징
     * @return
     */
    List<Used> purchase(PrincipalDetails principalDetails,Criteria criteria);

    /**
     * 판매목록
     * @param principalDetails
     * @param criteria 페이징
     * @return
     */
    List<Used> sales(PrincipalDetails principalDetails,Criteria criteria);

    /**
     * 페이징처리를 위한 총 갯수
     * @param memberId
     * @return
     */
    int countPurchase(long memberId);

    /**
     * 페이징처리를 위한 총 갯수
     * @param memberId
     * @return
     */
    int countSales(long memberId);

    /**
     * 페이징처리를 위한 총 갯수
     * @param memberId
     * @return
     */
    int countMyUsedLikePost(long memberId);
}
