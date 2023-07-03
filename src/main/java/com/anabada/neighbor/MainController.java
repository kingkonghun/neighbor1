package com.anabada.neighbor;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.used.service.UsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
    public String main(Model model) {
//        List<ClubResponse> clubList = clubService.findClubList(search);
        model.addAttribute("starList", usedService.mainList());
        model.addAttribute("clubList", clubService.mainList());
        return "index";
    }

}
