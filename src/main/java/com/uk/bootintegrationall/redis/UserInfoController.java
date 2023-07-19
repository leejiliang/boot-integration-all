package com.uk.bootintegrationall.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 */
@RestController
@RequestMapping("/user-info")
public class UserInfoController {

    public UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/test-redis")
    public void testRedis() {
        userInfoService.testRedis();
    }
}
