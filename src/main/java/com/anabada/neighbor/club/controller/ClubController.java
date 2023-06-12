package com.anabada.neighbor.club.controller;

import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.club.service.ImageUtils;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ClubController {

    private final ClubService clubService;
    private final ImageUtils imageUtils;

    @Autowired
    public ClubController(ClubService clubService, ImageUtils imageUtils) {
        this.clubService = clubService;
        this.imageUtils = imageUtils;
    }

    @GetMapping("/clubList")
    public String clubList(Model model) {
        model.addAttribute("clubList", clubService.findClubList());
        return "club/clubList";
    }

    @GetMapping("/clubSave")
    public String clubSave() {
        return "club/clubSave";
    }

    @PostMapping("/clubSave")
    public String clubSave(ClubRequest clubRequest, Model model, HttpSession session) {
        Post post = Post.builder()
                .memberId((long)session.getAttribute("memberId"))
                .title(clubRequest.getTitle())
                .content(clubRequest.getContent())
                .postType("club")
                .build();
        long postId = clubService.savePost(post);
        Club club = Club.builder()
                .postId(postId)
                .hobbyId(clubService.findHobbyId(clubRequest.getHobbyName()))
                .maxMan(clubRequest.getMaxMan())
                .build();
        clubService.saveClub(club);
        List<ImageRequest> images = imageUtils.uploadImages(clubRequest.getImages());
        clubService.saveImages(postId, images);
        model.addAttribute("result", "글 등록성공!");
        return "club/clubSave";
    }

    @GetMapping("club/detail")
    public String clubDetail(Model model, long postId) {
        ClubResponse response = clubService.findClub(postId);
        model.addAttribute("postId", postId);
        return "club/clubDetail";
    }
}
