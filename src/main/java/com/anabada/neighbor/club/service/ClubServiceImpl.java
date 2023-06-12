package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.repository.ClubRepository;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override/*클럽세이브*/
    public int saveClub(Club club) {//post,club 등록
        if (clubRepository.insertClub(club) == 1) {//db등록 실패시 0으로반환
            return 1;
        }
        return 0;
    }

    @Override
    public long savePost(Post post) {
        if (clubRepository.insertPost(post) == 1){
            return post.getPostId();
        }
        //게시글이 성공적으로 등록되었으면 postId 반환 실패하였으면 -1반환
        return -1;
    }

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
    @Override
    public ClubResponse findClub(long postId) {
        ClubResponse response;
        Post post = clubRepository.selectPost(postId);
        Club club = clubRepository.selectClub(postId);

        return null;
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
