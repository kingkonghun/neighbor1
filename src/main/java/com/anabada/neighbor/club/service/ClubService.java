package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.used.domain.Post;

import java.util.List;

public interface ClubService {
    /**
     * club 저장 후 성공 or 실패 반환
     *
     * @param club 클럽게시글정보
     * @return 성공하면1 실패하면0
     */
    public int saveClub(Club club);//clubSave

    /**
     * post 저장 후 postId 반환 실패하면 -1 반환
     * @param post 포스트게시글정보
     * @return 성공하면 postId 실패하면 -1 반환
     */
    public long savePost(Post post);//생성된 게시글의 postId 반환

    /**
     * 이미지 정보 db저장
     * @param postId 게시물아이디 pk
     * @param images 요청받은 이미지리스트
     * @return 성공하면 1, 실패 or 이미지가없으면 0 리턴
     */
    public int saveImages(final Long postId, final List<ImageRequest> images);//이미지저장

    /**
     * 게시글 상세정보 조회
     * @param postId pk
     * @return 게시글 상세정보
     */
    public ClubResponse findClub(long postId);

    public long updatePost(Post post);

    public long updateClub(Club club);

    public long deletePost(long postId);
    /**
     * 취미아이디 찾기
     * @param hobbyName 취미이름
     * @return hobbyId 취미아이디
     */
    public long findHobbyId(String hobbyName);

    public List<ClubResponse> findClubList();

    public int checkPost(ClubRequest clubRequest);//clubPost객체의 Null값체크

}
