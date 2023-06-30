package com.anabada.neighbor.file.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(Model model, MaxUploadSizeExceededException e) {
        model.addAttribute("message", "파일용량이 너무 큽니다");
        return "upload_form";
    }
}
