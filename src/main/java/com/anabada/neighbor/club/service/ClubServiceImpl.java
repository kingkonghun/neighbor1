package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.ImageResponse;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.repository.ClubRepository;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Transactional
    @Override/*클럽세이브*/
    public int saveClub(Club club) {//post,club 등록
        if (clubRepository.insertClub(club) == 1) {//db등록 실패시 0으로반환
            return 1;
        }
        return 0;
    }
    @Transactional
    @Override
    public long savePost(Post post) {
        if (clubRepository.insertPost(post) == 1){
            return post.getPostId();
        }//게시글이 성공적으로 등록되었으면 postId 반환 실패하였으면 -1반환
        return -1;
    }
    @Transactional
    @Override
    public int saveImages(Long postId, List<ImageRequest> images) {
        if (CollectionUtils.isEmpty(images)) {//리스트의 길이가 0이면 0으로 리턴
            return 0;
        }
        for (ImageRequest image : images) {
            image.setPostId(postId);
            clubRepository.insertImage(image);
        }
        return 1;
    }

    /**
     * 이미지 리스트 조회
     * @param postId 게시글 번호 FK
     * @return 이미지 리스트
     */
    @Override
    public List<ImageResponse> findAllImageByPostId(Long postId) {
        return clubRepository.selectImagesByPostId(postId);
    }

    /**
     * 이미지 조회
     *
     * @param imgIds PK
     * @return 이미지
     */
    @Override
    public List<ImageResponse> findAllImageByImgIds(List<Long> imgIds) {
        if (CollectionUtils.isEmpty(imgIds)){
            return Collections.emptyList();//비어있는 리스트 반환
        }
        List<ImageResponse> result = new ArrayList<>();
        for(Long imgId : imgIds) {
            result.add(clubRepository.selectImageByImgId(imgId));
        }
        return result;
    }

    @Override
    public ImageResponse findImageByImgId(Long imgId) {
        return clubRepository.selectImageByImgId(imgId);
    }

    /**
     * 이미지 삭제(from DataBase)
     *
     * @param imgIds PK
     */
    @Transactional
    @Override
    public void deleteAllImageByImgIds(List<Long> imgIds) {
        if (CollectionUtils.isEmpty(imgIds)) {
            return;
        }
        for (Long imgId : imgIds) {
            clubRepository.deleteImageByImgId(imgId);
        }
    }

    @Override
    public ClubResponse findClub(long postId) {
        Post post = clubRepository.selectPost(postId);
        Member member = clubRepository.selectMember(post.getMemberId());
        Club club = clubRepository.selectClub(postId);
        return ClubResponse.builder()
                .postId(post.getPostId())
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .title(post.getTitle())
                .content(post.getContent())
                .hobbyName(clubRepository.selectHobbyName(club.getHobbyId()))
                .score(member.getScore())
                .maxMan(club.getMaxMan())
                .nowMan(club.getNowMan())
                .build();
    }

    @Transactional
    @Override
    public long updatePost(Post post) {
        clubRepository.updatePost(post);
        return post.getPostId();
    }

    @Transactional
    @Override
    public long updateClub(Club club) {
        clubRepository.updateClub(club);
        return club.getPostId();
    }

    @Override
    public long deletePost(long postId) {
        clubRepository.deletePost(postId);
        return postId;
    }

    @Override
    public long findHobbyId(String hobbyName) {
        return clubRepository.selectHobbyId(hobbyName);
    }

    @Override
    public List<ClubResponse> findClubList() {
        List<ClubResponse> result = new ArrayList<>(); //반환해줄 리스트생성
        List<Post> postList = clubRepository.selectPostList(); //foreach돌릴 postlist생성j
        for (Post p : postList) {
            Club club = clubRepository.selectClub(p.getPostId());//클럽객체가져오기
            if (club == null) { //클럽 널체크
                continue;
            }
            Member member = clubRepository.selectMember(p.getMemberId());//멤버객체가져오기
            ClubResponse temp = ClubResponse.builder()
                    .postId(p.getPostId())
                    .memberId(p.getMemberId())
                    .memberName(member.getMemberName())
                    .title(p.getTitle())
                    .content(p.getContent())
                    .hobbyName(clubRepository.selectHobbyName(club.getHobbyId()))
                    .score(member.getScore())
                    .maxMan(club.getMaxMan())
                    .nowMan(club.getNowMan())
                    .build();
            result.add(temp);
        }
        return result;
    }

    @Override
    public int checkPost(ClubRequest clubRequest) {//clubPost 정상값인지 체크 나중에memberId추가해야함
        if (clubRequest.getTitle() == null ||
                clubRequest.getContent() == null ||
                clubRequest.getHobbyName() == null ||
                clubRequest.getMaxMan() == null) {
            return 0;
        }
        return 1;
    }
}
