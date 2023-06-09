package com.anabada.neighbor.used.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ImgDownServiceImpl implements ImgDownService{
    public void imgDown(String filename, HttpServletResponse response)throws IOException {
        String uploadDir = "C:\\users\\upload_anabada\\";
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

}
