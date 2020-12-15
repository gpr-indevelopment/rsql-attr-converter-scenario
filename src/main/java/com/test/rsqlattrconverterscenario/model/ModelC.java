package com.test.rsqlattrconverterscenario.model;

import com.test.rsqlattrconverterscenario.custom.CustomString;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class ModelC {

    @Id
    @GeneratedValue
    private Long id;

    private final CustomString customString = CustomString.fromString("SOME_STRING");
}
