package com.uk.bootintegrationall.springmvc;

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
}