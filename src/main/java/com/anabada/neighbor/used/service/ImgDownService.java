package com.anabada.neighbor.used.service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImgDownService {
    public void downProfileImg(String profileImg, HttpServletResponse response) throws IOException;
}
