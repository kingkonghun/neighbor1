package com.anabada.neighbor.used.controller;

import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/used")
@RequiredArgsConstructor
public class UsedController {
    private final UsedService usedService;

    @GetMapping("/list")
    public List<Used> list(){
        return usedService.list();
    }

}
