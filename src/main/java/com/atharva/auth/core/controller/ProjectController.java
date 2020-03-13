package com.atharva.auth.core.controller;

import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/register")
    public ErrorCodes register(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        return projectService.add(id, split[1]);
    }

    @GetMapping("/verify")
    public ErrorCodes verify(@RequestHeader String auth) {
        final String[] split = auth.split(":");
        final String id = new String(Base64.getDecoder().decode(split[0]));
        return projectService.verify(id, split[1]);
    }

    @GetMapping("/getCreds")
    public String getCreds(@RequestParam String id) {
        return projectService.getKey(id);
    }
}
