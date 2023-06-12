package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.used.domain.Post;

import java.util.List;

public interface ClubService {
    /**
     * 클럽게시글 저장 post
     *
     * @param club 클럽
     * @return 성공하면1 실패하면0
     */
    public int saveClub(Club club);//clubSave

    /**
     * 포스트 저장
     * @param post 포스트
     * @return postId
     */
    public long savePost(Post post);//생성된 게시글의 postId 반환

    /**
     *
     * @param postId 게시물아이디
     * @param images 요청받은 이미지리스트
     * @return 성공하면 1, 실패 or 이미지가없으면 0 리턴
     */
    public int saveImages(final Long postId, final List<ImageRequest> images);//이미지저장

    public ClubResponse findClub(long postId);

    /**
     * 취미아이디 찾기
     * @param hobbyName 취미이름
     * @return hobbyId 취미아이디
     */
    public long findHobbyId(String hobbyName);

    public List<ClubResponse> findClubList();

    public int checkPost(ClubRequest clubRequest);//clubPost객체의 Null값체크

}
