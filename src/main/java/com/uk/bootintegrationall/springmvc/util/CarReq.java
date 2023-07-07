package com.uk.bootintegrationall.springmvc.util;

import com.uk.bootintegrationall.springmvc.validation.CarNameLegal;

import javax.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * @Description TODO
 */
public class CarReq {

    @CarNameLegal(message = "车名不合法, 里面有dog")
    private String carName;

    private String carColor;
    @Max(value = 9999999, message = "价格不能超过9999999")
    private BigDecimal carPrice;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public BigDecimal getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(BigDecimal carPrice) {
        this.carPrice = carPrice;
    }

    @Override
    public String toString() {
        return "CarReq{" +
            "carName='" + carName + '\'' +
            ", carColor='" + carColor + '\'' +
            ", carPrice=" + carPrice +
            '}';
    }
}
