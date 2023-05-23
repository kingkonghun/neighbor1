package com.anabada.neighbor.used.service;

import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Used;

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
    public void update(long postId);
    public void delete(long postId);
    public Used detail(long postId);
    public List<Img> images(long postId);

}
