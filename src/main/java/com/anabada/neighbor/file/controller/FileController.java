package com.anabada.neighbor.file.controller;

import com.anabada.neighbor.file.domain.ResponseMessage;
import com.anabada.neighbor.file.service.FileService;
import com.anabada.neighbor.file.service.FileUtils;
import com.anabada.neighbor.file.service.FilesStorageServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController {

    @Autowired
    FileUtils fileUtils;

    @DeleteMapping("/files/{saveName:.+}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String saveName, String createDate ) {
        String message = "";

        try {
            boolean existed = fileUtils.delete(saveName, createDate);

            if (existed) {
                message = "파일을 성공적으로 삭제하였습니다.: " + saveName;
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }

            message = "파일이 존재하지 않습니다.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "파일을 삭제할 수 없습니다. : " + saveName + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }

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
