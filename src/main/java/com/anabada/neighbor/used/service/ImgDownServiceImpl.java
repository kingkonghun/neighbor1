package com.anabada.neighbor.used.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class ImgDownServiceImpl implements ImgDownService{
    public void imgDown(String filename, HttpServletResponse response)throws IOException {
//        String uploadDir = "C:\\upload_anabada\\";
        String uploadDir = "/Users/upload_anabada/";
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
//        String uploadDir = "C:\\upload_anabada\\profile\\";
        String uploadDir = "/Users/upload_anabada/profile/";
        String path = "";
        OutputStream out = response.getOutputStream();
        if (profileImg.equals("defaultImg.png")) {//프로필이미지가 default면 static에서 이미지
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream(defaultDir + profileImg);
            if (is != null) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                String encodedFilename = new String(profileImg.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                response.setHeader("Content-Disposition", "attachment;filename=" + encodedFilename);
                while ((bytesRead = is.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                out.flush();
                out.close();
                is.close();
                return;
            }else{
                System.out.println("디폴트 사진을 찾을 수 없음");
            }
        }else{//프로필 이미지가 defaultImg.png가 아니면
            path = uploadDir+profileImg;
            FileInputStream fis = new FileInputStream(path);
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
}
