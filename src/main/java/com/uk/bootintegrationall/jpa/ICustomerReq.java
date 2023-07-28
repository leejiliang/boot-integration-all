package com.uk.bootintegrationall.jpa;

import org.springframework.data.web.JsonPath;
import org.springframework.data.web.ProjectedPayload;

/**
 * @Description TODO
 */
@ProjectedPayload
public interface ICustomerReq {

    @JsonPath("$.name")
    Integer getName();
}
