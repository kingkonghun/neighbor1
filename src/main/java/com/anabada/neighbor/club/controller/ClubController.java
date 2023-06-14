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
import org.springframework.web.bind.annotation.RequestParam;

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
    public String clubSave(@RequestParam(value = "postId", required = false) Long postId
            , HttpSession session, Model model) {
        if (postId != null) {
            ClubResponse clubResponse = clubService.findClub(postId);
            model.addAttribute("club", clubResponse);
        }else {
            model.addAttribute("club", new ClubResponse());
        }
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
        if (postId == -1){
            model.addAttribute("result", "글 등록실패!");
            return "club/clubSave";
        }
        Club club = Club.builder()
                .postId(postId)
                .memberId((long) session.getAttribute("memberId"))
                .hobbyId(clubService.findHobbyId(clubRequest.getHobbyName()))
                .maxMan(clubRequest.getMaxMan())
                .build();
        if (clubService.saveClub(club) == 1){
            List<ImageRequest> images = imageUtils.uploadImages(clubRequest.getImages());
            clubService.saveImages(postId, images);
            model.addAttribute("result", "글 등록성공!");
        }else{
            model.addAttribute("result", "글 등록실패!");
        }
        return "redirect:clubList";
    }

    @GetMapping("/clubDetail")
    public String clubDetail(Model model, long postId) {
        ClubResponse response = clubService.findClub(postId);
        model.addAttribute("postId", postId);
        return "club/clubDetail";
    }

    @GetMapping("/clubRemove")
    public String clubRemove(Long postId) {
        clubService.deletePost(postId);
        return "redirect:clubList";
    }
}
