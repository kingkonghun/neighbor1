package com.anabada.neighbor.used.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ImgDownServiceImpl implements ImgDownService{
    public void imgDown(String filename, HttpServletResponse response)throws IOException {
        String uploadDir = "C:\\upload_anabada\\";
        String path = uploadDir+filename;
        FileInputStream fis = new FileInputStream(path);
        OutputStream out = response.getOutputStream();

        String encodedFilename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFilename);
        byte b[] = new byte[4096];
        int numRead;
        while (true){
            numRead = fis.read(b,0,b.length);
            if(numRead == -1) break;
            out.write(b,0,numRead);
        }
        out.flush();
        out.close();
        fis.close();
    }

    @Override
    public void downProfileImg(String profileImg, HttpServletResponse response) throws IOException {
        String defaultDir = "static/img/";
        String uploadDir = "C:\\upload_anabada\\";
        String path = "";
        if (profileImg.equals("defaultImg.png")) {//프로필이미지가 default면 static에서 이미지
            path=defaultDir+profileImg;
        }else{path = uploadDir+profileImg;}

        FileInputStream fis = new FileInputStream(path);
        OutputStream out = response.getOutputStream();

        String encodedFilename = new String(profileImg.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFilename);
        byte b[] = new byte[4096];
        int numRead;
        while (true){
            numRead = fis.read(b,0,b.length);
            if(numRead == -1) break;
            out.write(b,0,numRead);
        }
        out.flush();
        out.close();
        fis.close();
    }
}
