package com.anabada.neighbor.club.controller;

import com.anabada.neighbor.club.domain.ClubPost;
import com.anabada.neighbor.club.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClubController {

    private final ClubService clubService;
    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/clubSave")
    public String clubSave() {
        return "club/clubSave";
    }

    @PostMapping("/clubSave")
    public String clubSave(ClubPost clubPost, Model model) {
        if(clubService.clubSave(clubPost) == 1){
            model.addAttribute("result","글 등록성공!");
        }else{
            model.addAttribute("result", "글 등록실패!");
        }
       return "club/clubSave";
    }

    @GetMapping("/clublist")
    @ResponseBody
    public void clubList(){
    }
}
