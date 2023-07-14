package com.uk.bootintegrationall.springmvc.config;

import com.uk.bootintegrationall.springmvc.CarBanner;
import org.springframework.core.convert.converter.Converter;

/**
 * @Description TODO
 */
public class StringToEnumConverter implements Converter<String, CarBanner> {
    @Override
    public CarBanner convert(String source) {
        try {
            return CarBanner.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}