package com.uk.bootintegrationall.springmvc;

import com.uk.bootintegrationall.springmvc.config.UserInfo;
import com.uk.bootintegrationall.springmvc.exception.ServerException;
import com.uk.bootintegrationall.springmvc.exception.ServerExceptionEnum;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @Description TODO
 */
@RestController
@RequestMapping("/car")
public class CarController {

    @GetMapping("/getCar")
    public String getCar(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate produceDate) {
        System.out.println("produceDate = " + produceDate);
        return "car";
    }

    @GetMapping("/getCar2")
    public String getCar2(@RequestParam LocalDate produceDate) {
        System.out.println("produceDate = " + produceDate);
        return "car";
    }

    @GetMapping("/getCar3")
    public String getCar3(@RequestParam LocalDate produceDate) {
        System.out.println("produceDate = " + produceDate);
        if (1 == 1) {
            throw new ServerException(ServerExceptionEnum.SERVER_ERROR);
        }
        return "car";
    }

    @GetMapping("/getCar4")
    public String getCar4(UserInfo userInfo) {
        return userInfo.toString();
    }
}
