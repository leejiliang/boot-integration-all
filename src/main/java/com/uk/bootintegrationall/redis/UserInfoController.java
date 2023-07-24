package com.uk.bootintegrationall.redis;

import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/cacheable")
    public UserInfo testCacheable(@RequestParam String userId){
//        userInfoService.getCarInfo(userId);
//        userInfoService.getBannerInfo(userId);
//        userInfoService.getBillInfo(userId);
        return userInfoService.getUserInfo(userId);
    }

    @DeleteMapping("/cacheable")
    public void deleteUser(@RequestParam String userId) {
        userInfoService.deleteUserInfo(userId);
    }

    @PutMapping("/cacheable")
    public UserInfo updateUserInfo(@RequestParam String userId) {
        return userInfoService.updateUserInfo(userId);
    }
}
