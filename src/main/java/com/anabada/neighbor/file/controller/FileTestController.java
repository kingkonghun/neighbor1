package com.anabada.neighbor.file.controller;

import com.anabada.neighbor.file.domain.FileInfo;
import com.anabada.neighbor.file.service.FilesStorageServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileTestController {

    @Autowired
    FilesStorageServiceTest storageService;

    @GetMapping("/images/new")
    public String newImage(Model model) {
        return "upload_form";
    }

    @PostMapping("/images/upload")
    public String uploadImage(Model model, @RequestParam("file") MultipartFile file) {
        String message = "";

        try {
            storageService.save(file);
            message = "이미지를 성공적으로 업로드 했습니다 : " + file.getOriginalFilename();
            model.addAttribute("message", message);
        } catch (Exception e) {
            message = "이미지 업로드에 실패하였습니다 : " + file.getOriginalFilename();
            model.addAttribute("message", message);
        }
        return "upload_form";
    }

    @GetMapping("/images")
    public String getListImages(Model model) {
        System.out.println(storageService.loadAll().collect(Collectors.toList()));
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileTestController.class, "getImage"
                            , path.getFileName().toString(), "1").build().toString();
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
        System.out.println(fileInfos);

        model.addAttribute("images", fileInfos);

        return "images";
    }

}
