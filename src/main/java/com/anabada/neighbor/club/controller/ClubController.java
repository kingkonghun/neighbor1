package com.anabada.neighbor.club.controller;

import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.ImageResponse;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.club.service.ImageUtils;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.service.FilesStorageService;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ClubController {

    private final ClubService clubService;
    private final ImageUtils imageUtils;
    private final FilesStorageService storageService;

    @Autowired
    public ClubController(ClubService clubService, ImageUtils imageUtils, FilesStorageService storageService) {
        this.clubService = clubService;
        this.imageUtils = imageUtils;
        this.storageService = storageService;
    }

    @GetMapping("/clubList")
    public String clubList(Model model) {
        model.addAttribute("clubList", clubService.findClubList());
        return "club/clubList";
    }

    //게시글 작성 페이지
    @GetMapping("/clubSave")
    public String clubSave(@RequestParam(value = "postId", required = false) Long postId
            , HttpSession session, Model model) {
        if (postId != null) {//postId가 있으면 검색해서 정보 가져오기
            ClubResponse clubResponse = clubService.findClub(postId);
            model.addAttribute("club", clubResponse);
        } else {
            model.addAttribute("club", new ClubResponse());
        }
        return "club/clubSave";
    }

    @PostMapping("/clubSave")
    public String clubSave(ClubRequest clubRequest, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Post post = Post.builder()
                .memberId(principalDetails.getMember().getMemberId())
                .title(clubRequest.getTitle())
                .content(clubRequest.getContent())
                .postType("club")
                .build();
        long postId = clubService.savePost(post);
        if (postId == -1) {
            model.addAttribute("result", "글 등록실패!");
            return "club/clubSave";
        }
        Club club = Club.builder()
                .postId(postId)
                .memberId(post.getMemberId())
                .hobbyId(clubService.findHobbyId(clubRequest.getHobbyName()))
                .maxMan(clubRequest.getMaxMan())
                .build();
        if (clubService.saveClub(club) == 1) {
            List<ImageRequest> images = imageUtils.uploadImages(clubRequest.getImages());
            clubService.saveImages(postId, images);
            model.addAttribute("result", "글 등록성공!");//나중에 삭제
        } else {
            model.addAttribute("result", "글 등록실패!");//나중에 삭제
        }
        return "redirect:clubList";
    }

    @GetMapping("/clubDetail")
    public String clubDetail(@RequestParam(value = "postId", required = false) Long postId, Model model) {
        ClubResponse response = clubService.findClub(postId);

        model.addAttribute("postId", postId);
        return "club/clubDetail";
    }

    @GetMapping("/clubRemove")
    public String clubRemove(Long postId) {
        clubService.deletePost(postId);
        return "redirect:clubList";
    }

    // 파일 리스트 조회
    @GetMapping("/posts/{postId}/images")
    @ResponseBody
    public List<ImageResponse> clubImage(@PathVariable Long postId) {
        return clubService.findAllImageByPostId(postId);
    }

    @GetMapping("/testest")
    public String getListImages(Model model) {
        return null;
    }

}
