package com.anabada.neighbor.used.service;

import com.anabada.neighbor.used.domain.Category;
import com.anabada.neighbor.used.domain.Used;

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
    public List<Used> list(long categoryId);
    public void write(Used used)throws Exception;
    public void update(Used used) throws Exception;
    public void delete(long postId);
    public Used detail(long postId, HttpServletRequest request, HttpServletResponse response);
    public String findImgUrl(long postId);//이미지 이름가져오기


    public void downloadFiles(String filenames, HttpServletResponse response) throws IOException;

    public List<Category> categoryList();

//    public List<Used> similarList(long categoryId);
}
