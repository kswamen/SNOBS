package com.back.snobs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class TestChatRoomController {
    @GetMapping(value = "/index")
    public String test() {
        return "test2";
    }

    @GetMapping(value = "/index2")
    public String test2() {
        return "test3";
    }
}
