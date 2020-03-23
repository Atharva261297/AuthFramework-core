package com.atharva.auth.core.controller;

import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.service.HashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger(HashController.class);

    @GetMapping("/register")
    public ErrorCodes register(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        log.debug("Register 1 : " + id);
        return credService.add(id, split[1]);
    }

    @GetMapping("/admin/login")
    public ErrorCodes login(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        log.debug("Login 1 : " + id);
        return credService.verify(id, split[1]);
    }

    @GetMapping("/admin/change")
    public ErrorCodes change(@RequestHeader String new_auth, @RequestHeader String old_auth) {
        final String[] oldAuthSplit = old_auth.split(":");
        final String oldId = new String(Base64.getDecoder().decode(oldAuthSplit[0]));
        final String[] newAuthSplit = old_auth.split(":");
        final String newId = new String(Base64.getDecoder().decode(oldAuthSplit[0]));
        return credService.update(oldId, oldAuthSplit[1], newId, newAuthSplit[1]);
    }

    @GetMapping("/admin/reset")
    public ErrorCodes reset(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        log.debug("Reset 1 : " + id);
        return credService.reset(id, split[1]);
    }
}
