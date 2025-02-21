package com.rcr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSR {
    @GetMapping
    public String api(){
        return "Jai Sri Ram";
    }
}
