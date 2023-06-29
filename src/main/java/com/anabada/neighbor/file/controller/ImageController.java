package com.anabada.neighbor.file.controller;

import com.anabada.neighbor.file.domain.ImageInfo;
import com.anabada.neighbor.file.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ImageController {

    @Autowired
    FilesStorageService storageService;

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
        List<ImageInfo> imageInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ImageController.class, "getImage"
                            , path.getFileName().toString()).build().toString();
            return new ImageInfo(filename, url);
        }).collect(Collectors.toList());
        System.out.println(imageInfos);

        model.addAttribute("images", imageInfos);

        return "images";
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + file.getFilename() + "\"").body(file);
    }
}
