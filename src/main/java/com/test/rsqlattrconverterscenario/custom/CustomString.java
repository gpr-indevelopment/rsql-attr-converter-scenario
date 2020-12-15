package com.test.rsqlattrconverterscenario.custom;

public class CustomString extends CustomAttribute<String>{

    public static CustomString fromString(String input) {
        CustomString customString = new CustomString();
        customString.setValue(input);
        return customString;
    }

    public static CustomString valueOf(String string) {
        return CustomString.fromString(string);
    }
}
