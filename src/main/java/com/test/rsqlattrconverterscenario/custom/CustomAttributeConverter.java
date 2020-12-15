package com.test.rsqlattrconverterscenario.custom;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CustomAttributeConverter implements AttributeConverter<CustomString, String> {


    @Override
    public String convertToDatabaseColumn(CustomString attribute) {
        return attribute.getValue();
    }

    @Override
    public CustomString convertToEntityAttribute(String dbData) {
        return CustomString.fromString(dbData);
    }
}
