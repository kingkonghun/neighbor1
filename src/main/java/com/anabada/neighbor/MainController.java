package com.anabada.neighbor;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.service.UsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.lang.model.SourceVersion;
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
        List<ClubResponse> clubList = clubService.findClubList();
        model.addAttribute("starList", usedService.mainList());
        model.addAttribute("clubList", clubList.subList(0, Math.min(clubList.size(),6)));
        return "index";
    }

}
