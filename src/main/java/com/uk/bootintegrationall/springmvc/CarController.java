package com.uk.bootintegrationall.springmvc;

import com.uk.bootintegrationall.springmvc.config.UserInfo;
import com.uk.bootintegrationall.springmvc.exception.ServerException;
import com.uk.bootintegrationall.springmvc.exception.ServerExceptionEnum;
import com.uk.bootintegrationall.springmvc.util.CarReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.modelmbean.ModelMBean;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

/**
 * @Description TODO
 */
@RestController
@RequestMapping("/car")
@Validated
//@CrossOrigin(origins = "http://localhost:8080", methods = RequestMethod.GET)
public class CarController {

    @Autowired
    private StorageService storageService;
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

    @GetMapping("/getCar5")
    public String getCar5(@RequestParam @Min(1970) @Max(value = 9999, message = "年份信息不合法") Integer year) {
        return year.toString();
    }

    @PostMapping("/getCar6")
    public String getCar5(@RequestBody @Valid CarReq carReq) {
        return carReq.toString();
    }

//    @CrossOrigin(origins = "http://localhost:8080", methods = RequestMethod.GET)
    @GetMapping("/getCar7")
    public String getCar7() {
        return "car7";
    }

    @PostMapping("/upload/image")
    public void uploadCarImage(@RequestParam MultipartFile file, ModelMap modelMap) {
        modelMap.addAttribute("file", file);
        storageService.store(file);
    }
}
