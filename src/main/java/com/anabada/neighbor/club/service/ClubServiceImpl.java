package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.club.domain.PostSave;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.domain.entity.Hobby;
import com.anabada.neighbor.club.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public int clubSave(ClubPost clubPost) {//post,club 등록
        if (clubPostCheck(clubPost) == -1) {
            return 0; //-1이면 clubPost에 문제가있으니 0으로 반환
        }
        Club club = Club.builder().postId(clubPostSave(clubPost))//성공하면 PostId값 반환)
                .memberId(clubPost.getMemberId())//memberid가져오기
                .hobbyId(findByHobbyId(clubPost.getHobbyName()))//hobbyid가져오기
                .maxMan(clubPost.getMaxMan())//maxman가져오기
                .build();
        if (clubRepository.clubSave(club) == 0) {//db등록 실패시 0으로반환
            return 0;
        }
        return 1;
    }

    @Override
    public long clubPostSave(ClubPost clubPost) {//게시글이 성공적으로 등록되었으면 postId 반환 실패하였으면 -1반환
        PostSave post = PostSave.builder().memberId(clubPost.getMemberId())
                .title(clubPost.getTitle())
                .content(clubPost.getContent())
                .postType("club")//클럽타입
                .build();
        if (clubRepository.clubPostSave(post) == 1) {
            return post.getPostId();
        }else{
            return -1;
        }
    }

    @Override
    public long findByHobbyId(String hobbyName) {
        return clubRepository.findByHobbyId(hobbyName);
    }

    @Override
    public int clubPostCheck(ClubPost clubPost) {//clubPost 정상값인지 체크 나중에memberId추가해야함
        if (clubPost.getTitle() == null ||
                clubPost.getContent() == null ||
                clubPost.getHobbyName() == null ||
                clubPost.getMaxMan() == null) {
            return 0;
        }
        return 1;
    }

}
