package com.atharva.auth.core.controller;

import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.service.AdminHashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/auth/admin")
public class AdminHashController {

    @Autowired
    private AdminHashService credService;

    Logger log = LoggerFactory.getLogger(UserHashController.class);

    @PostMapping("/register")
    public ErrorCodes register(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        log.debug("Register 1 : " + id);
        return credService.add(id, split[1]);
    }

    @PostMapping("/login")
    public ErrorCodes login(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        log.debug("Login 1 : " + id);
        return credService.verify(id, split[1]);
    }

    @PostMapping("/change")
    public ErrorCodes change(@RequestHeader String new_auth, @RequestHeader String old_auth) {
        final String[] oldAuthSplit = old_auth.split(":");
        final String oldId = new String(Base64.getDecoder().decode(oldAuthSplit[0]));
        final String[] newAuthSplit = new_auth.split(":");
        final String newId = new String(Base64.getDecoder().decode(oldAuthSplit[0]));
        return credService.update(oldId, oldAuthSplit[1], newId, newAuthSplit[1]);
    }

    @PostMapping("/reset")
    public ErrorCodes reset(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        log.debug("Reset 1 : " + id);
        return credService.reset(id, split[1]);
    }
}
