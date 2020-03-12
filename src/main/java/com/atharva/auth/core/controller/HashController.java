package com.atharva.auth.core.controller;

import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.service.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/auth")
public class HashController {

    @Autowired
    private HashService credService;

    @GetMapping("/register")
    public ErrorCodes register(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        return credService.add(id, split[1]);
    }
}
