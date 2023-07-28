package com.uk.bootintegrationall.jpa.web;

import com.uk.bootintegrationall.jpa.ICustomerReq;
import org.springframework.web.bind.annotation.*;

/**
 * @Description TODO
 */
@RestController
@RequestMapping("/customer")
public class CustomerWeb {

    @PostMapping("/create")
    public ICustomerReq getCustomer(@RequestBody ICustomerReq re) {
        return re;
    }
}
