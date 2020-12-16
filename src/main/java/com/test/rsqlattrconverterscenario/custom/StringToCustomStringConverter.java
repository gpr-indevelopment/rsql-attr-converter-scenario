package com.test.rsqlattrconverterscenario.custom;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCustomStringConverter implements Converter<String, CustomString> {

    @Override
    public CustomString convert(String source) {
        return CustomString.fromString(source);
    }
}
