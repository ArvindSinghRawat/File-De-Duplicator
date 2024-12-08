package com.asr.example.file.deduplicator.view;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
}
