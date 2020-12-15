package com.test.rsqlattrconverterscenario.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomAttribute<T> {

    private T value;
}
