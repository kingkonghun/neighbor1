package com.anabada.neighbor.used.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Service
public class ImgDownServiceImpl implements ImgDownService{
   private final String DEFAULT_DIR = "static/img/";
    private final String UPLOAD_PATH = Paths.get(System.getProperty("user.home"), "upload_anabada","profile").toString();

    @Override
    public void downProfileImg(String profileImg, HttpServletResponse response) throws IOException {

        String path = "";
        OutputStream out = response.getOutputStream();
        if (profileImg.equals("defaultImg.png")) {//프로필이미지가 default면 static에서 이미지
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream(DEFAULT_DIR + profileImg);
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
            path = Paths.get(UPLOAD_PATH,profileImg).toString();
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
