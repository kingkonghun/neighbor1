package com.anabada.neighbor.club.controller;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.club.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClubController {

    private final ClubService clubService;
    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("club")
    public String clubMain() {
        return "club/club";
    }

    @PostMapping("/clubPost")
    public void clubPost() {
//        return null;
    }

    @GetMapping("/clublist")
    @ResponseBody
    public ClubPost clubList(){
        ClubPost result = clubService.clubPostList();
        return result;
    }
}
