package com.anabada.neighbor.club.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ClubController {
    @GetMapping("club")
    public String clubMain() {
        return "club";
    }

    @PostMapping("/clubPost")
//    @RequestBody
    public void clubPost(){
//        return null;
    }
}
