package com.anabada.neighbor.member.used.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.used.domain.Category;
import com.anabada.neighbor.member.used.domain.Report;
import com.anabada.neighbor.member.used.domain.ReportType;
import com.anabada.neighbor.member.used.domain.Used;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UsedService {
    //글쓰기
    //글수정
    //글삭제
    //글리스트
    //상세보기
    //사진
    public List<Used> list(long categoryId, String listType, int num);
    public void write(Used used, PrincipalDetails principalDetails)throws Exception;
    public void update(Used used, PrincipalDetails principalDetails) throws Exception;
    public void delete(long postId);
    public Used detail(long postId, HttpServletRequest request, HttpServletResponse response, PrincipalDetails principalDetails);
    public String findImgUrl(long postId);//이미지 이름가져오기
    public void downloadFiles(String filenames, HttpServletResponse response) throws IOException;
    List<Category> categoryList();
    List<Used> mainList();
    Used likes(long postId, PrincipalDetails principalDetails, int likesCheck);

    List<ReportType> reportType();

    void report(Report report, PrincipalDetails principalDetails);
}
