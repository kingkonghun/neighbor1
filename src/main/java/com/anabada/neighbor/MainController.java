package com.anabada.neighbor;

import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.used.service.UsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.lang.model.SourceVersion;

@Controller
public class MainController {
    private final UsedService usedService;
    private final ClubService clubService;
    @Autowired
    public MainController(UsedService usedService, ClubService clubService) {
        this.usedService = usedService;
        this.clubService = clubService;
    }
    @GetMapping("/")
    public String main(Model model){
        model.addAttribute("starList", usedService.mainList());
//        model.addAttribute("club", clubService.mainList());
        return "index";
    }

}
