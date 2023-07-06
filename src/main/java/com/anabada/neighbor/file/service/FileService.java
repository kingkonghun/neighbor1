package com.anabada.neighbor.file.service;

import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;

import java.util.List;

public interface FileService {

    /**
     * 이미지 정보 DB 저장
     * @param postId 게시물아이디 PK
     * @param files 요청받은 파일 리스트
     * @return 성공하면1, 실패 or 이미지가 없으면 0 리턴
     */
    public int saveFiles(final Long postId, final List<FileRequest> files);

    /**
     * 파일 리스트 조회
     * @param postId 게시글 번호 FK
     * @return 파일 리스트
     */
    public List<FileResponse> findAllFileByPostId(Long postId);

    /**
     * 파일 리스트 조회
     * @param ids PK 리스트
     * @return 파일 리스트
     */
    public List<FileResponse> findAllFileByIds(List<Long> ids);

    /**
     * 파일 조회
     * @param id PK
     * @return 파일정보
     */
    public FileResponse findFileById(Long id);

    /**
     * 이미지 삭제 (from DateBase)
     * @param fileResponses 파일정보 리스트
     */
    public void deleteAllFileByIds(List<FileResponse> fileResponses);
}
