package com.atharva.auth.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiagnoseController {
    @RequestMapping("/up")
    public String up() {
        return "1";
    }
}
