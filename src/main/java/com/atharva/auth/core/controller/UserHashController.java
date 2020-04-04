package com.atharva.auth.core.controller;

import com.atharva.auth.core.model.ErrorCodes;
import com.atharva.auth.core.model.Response;
import com.atharva.auth.core.service.UserHashService;
import com.atharva.auth.core.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/auth/user")
public class UserHashController {

    @Autowired
    private UserHashService credService;

    @Autowired
    private ProjectService projectService;

    Logger log = LoggerFactory.getLogger(UserHashController.class);

    @GetMapping("/register")
    public Response register(@RequestHeader String project_auth, @RequestHeader String user_auth) {
        // Verify project creds
        final String[] projectSplit = project_auth.split(":");
        final String projectId = new String(Base64.getDecoder().decode(projectSplit[0]));
        ErrorCodes code = projectService.verify(projectId, projectSplit[1]);

        if (code == ErrorCodes.SUCCESS) {
            final String[] userSplit = user_auth.split(":");
            final String id = new String(Base64.getDecoder().decode(userSplit[0]));
            return credService.add(id, projectId, userSplit[1]);
        }

        return new Response(code, null);
    }

    @GetMapping("/login")
    public Response login(@RequestHeader String project_auth, @RequestHeader String user_auth) {
        // Verify project creds
        final String[] projectSplit = project_auth.split(":");
        final String projectId = new String(Base64.getDecoder().decode(projectSplit[0]));
        ErrorCodes code = projectService.verify(projectId, projectSplit[1]);

        if (code == ErrorCodes.SUCCESS) {
            final String[] userSplit = user_auth.split(":");
            final String id = new String(Base64.getDecoder().decode(userSplit[0]));
            return credService.verify(id, projectId, userSplit[1]);
        }

        return new Response(code, null);
    }

    @GetMapping("/change")
    public ErrorCodes change(@RequestHeader String new_auth, @RequestHeader String old_auth, @RequestHeader String project_auth) {
        // Verify project creds
        final String[] projectSplit = project_auth.split(":");
        final String projectId = new String(Base64.getDecoder().decode(projectSplit[0]));
        ErrorCodes code = projectService.verify(projectId, projectSplit[1]);

        if (code == ErrorCodes.SUCCESS) {
            final String[] oldAuthSplit = old_auth.split(":");
            final String oldId = new String(Base64.getDecoder().decode(oldAuthSplit[0]));
            final String[] newAuthSplit = old_auth.split(":");
            final String newId = new String(Base64.getDecoder().decode(oldAuthSplit[0]));
            return credService.update(oldId, oldAuthSplit[1], newId, newAuthSplit[1], projectId);
        }

        return code;
    }

//    @GetMapping("/reset")
//    public ErrorCodes reset(@RequestHeader String auth) {
//        final String[] split = auth.split(":");
//        final String id = new String(Base64.getDecoder().decode(split[0]));
//        log.debug("Reset 1 : " + id);
//        return credService.reset(id, split[1]);
//    }
}
