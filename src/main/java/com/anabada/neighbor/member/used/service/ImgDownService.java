package com.anabada.neighbor.member.used.service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImgDownService {
    public void imgDown(String filename, HttpServletResponse response)throws IOException;

    public void downProfileImg(String profileImg, HttpServletResponse response) throws IOException;
}
