package com.anabada.neighbor.file.Repository;

import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository {
    /**
     * 요청받은 이미지 정보 DB에 넣기
     * @param fileRequest 이미지요청
     */
    public void insertFile(FileRequest fileRequest);

    /**
     * 이미지 리스트 조회
     * @param postId 게시글 번호 FK
     * @return 이미지 정보
     */
    public List<FileResponse> selectFilesByPostId(Long postId);

    /**
     * 이미지 리스트 조회
     * @param id PK
     * @return 이미지 정보
     */
    public FileResponse selectFileById(Long id);

    /**
     * 이미지 삭제
     * @param id PK
     */
    public void deleteFileById(Long id);

}
