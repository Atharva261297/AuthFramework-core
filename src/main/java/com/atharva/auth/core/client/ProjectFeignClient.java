package com.atharva.auth.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ProjectClient", url = "${url.admin.project}")
public interface ProjectFeignClient {

    @Async
    @RequestMapping(method = RequestMethod.POST, value = "/incrementUser/{count}")
    void incrementUser(@RequestParam String projectId, @PathVariable(name = "count") int count);
}
