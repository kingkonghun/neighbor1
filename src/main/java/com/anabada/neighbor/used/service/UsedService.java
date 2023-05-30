package com.anabada.neighbor.used.service;

import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Used;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

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
    public List<Used> list();
    public void write(Used used);
    public void update(Used used);
    public void delete(long postId);
    public Used detail(long postId);
    public String findImgUrl(long postId);//이미지 이름가져오기


    public void downloadFiles(String filenames, HttpServletResponse response) throws IOException;
}
