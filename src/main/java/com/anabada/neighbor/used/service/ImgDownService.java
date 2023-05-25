package com.anabada.neighbor.used.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ImgDownService {
    public void imgDown(String filename, HttpServletResponse response)throws IOException;
}
