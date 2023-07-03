package com.anabada.neighbor.file.controller;

import com.anabada.neighbor.file.service.FileService;
import com.anabada.neighbor.file.service.FileUtils;
import com.anabada.neighbor.file.service.FilesStorageServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController {

    @Autowired
    FileUtils fileUtils;

    @GetMapping("/images/{saveName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String saveName, String addPath) {
        Resource file;
//        if (createDate.equals("1")) {
//             file = fileUtils.load(originalName);
//        }else {
        file = fileUtils.load(saveName, addPath);
//        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + file.getFilename() + "\"").body(file);
    }



//    @Autowired
//    FilesStorageServiceTest storageService;
//
//    @GetMapping("/images/{originalName:.+}")
//    public ResponseEntity<Resource> getImage(@PathVariable String originalName, String createDate) {
//        Resource file;
//        if (createDate.equals("1")) {
//             file = storageService.load(originalName);
//        }else {
//             file = storageService.load(originalName, createDate);
//        }
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
//                        + file.getFilename() + "\"").body(file);
//    }
}
