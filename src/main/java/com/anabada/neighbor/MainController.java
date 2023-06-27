package com.anabada.neighbor;

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
        model.addAttribute("starList", usedService.mainList());
//        model.addAttribute("club", clubService.mainList());
        return "index";
    }

}
