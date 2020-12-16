package com.test.rsqlattrconverterscenario.custom;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomString extends CustomAttribute<String>{

//    public CustomString(String input) {
//        super();
//        this.setValue(input);
//    }

    public static CustomString fromString(String input) {
        CustomString customString = new CustomString();
        customString.setValue(input);
        return customString;
    }

    public static CustomString valueOf(String string) {
        return CustomString.fromString(string);
    }
}
