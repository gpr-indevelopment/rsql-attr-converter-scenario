package com.test.rsqlattrconverterscenario.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ImplA")
@Getter
@Setter
@AllArgsConstructor
public class ImplA extends Abstract{

    private boolean isAlive;

    private String path;
}
