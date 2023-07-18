package com.anabada.neighbor;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.file.domain.FileInfo;
import com.anabada.neighbor.file.service.FileUtils;
import com.anabada.neighbor.used.domain.Used;
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
    private final FileUtils fileUtils;

    @Autowired
    public MainController(UsedService usedService, ClubService clubService, FileUtils fileUtils) {
        this.usedService = usedService;
        this.clubService = clubService;
        this.fileUtils = fileUtils;
    }

    @GetMapping("/")
    public String main(Model model) {
        List<Used> usedList = usedService.mainList();
        List<ClubResponse> clubResponses = clubService.mainList();

        if (usedList != null) {
            for (Used used : usedList) {
                List<FileInfo> fileInfoList = fileUtils.getFileInfo(used.getFileResponseList());
                used.setFileInfo(fileInfoList.get(0));
            }
        }

        if (clubResponses != null) {
            for (ClubResponse clubResponse : clubResponses) {
                List<FileInfo> fileInfoList = fileUtils.getFileInfo(clubResponse.getFileResponseList());
                clubResponse.setFileInfo(fileInfoList.get(0));
            }
        }

        model.addAttribute("starList", usedList);
        model.addAttribute("clubList", clubResponses);
        return "index";
    }

}
