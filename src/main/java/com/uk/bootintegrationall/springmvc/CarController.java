package com.uk.bootintegrationall.springmvc;

import com.uk.bootintegrationall.springmvc.config.IgnoreAware;
import com.uk.bootintegrationall.springmvc.config.UserInfo;
import com.uk.bootintegrationall.springmvc.exception.ServerException;
import com.uk.bootintegrationall.springmvc.exception.ServerExceptionEnum;
import com.uk.bootintegrationall.springmvc.util.CarReq;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;

import javax.management.modelmbean.ModelMBean;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private ServletContext servletContext;
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

    @GetMapping("/image")
    public void getImageAsByteArray(HttpServletResponse response) throws IOException {
        InputStream inputStream= servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/image2")
    @IgnoreAware
    public @ResponseBody byte[] getImageAsByteArray() throws IOException {
        InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
        return org.apache.commons.io.IOUtils.toByteArray(in);
    }

    @RequestMapping(value = "/image3", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageAsResponseEntity() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        InputStream in = servletContext.getResourceAsStream("/WEB-INF/images/image-example.jpg");
        byte[] media = IOUtils.toByteArray(in);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }

    @ResponseBody
    @RequestMapping(value = "/image4", method = RequestMethod.GET)
    public Resource getImageAsResource() {
        return new ServletContextResource(servletContext, "/WEB-INF/images/image-example.jpg");
    }

    @RequestMapping(value = "/image5", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getImageAsResource2() {
        HttpHeaders headers = new HttpHeaders();
        Resource resource =
            new ServletContextResource(servletContext, "/public/云帆社区.jpeg");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }


}
