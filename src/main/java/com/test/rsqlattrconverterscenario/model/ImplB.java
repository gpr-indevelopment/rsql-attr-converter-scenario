package com.test.rsqlattrconverterscenario.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ImplB")
@Getter
@Setter
@Audited
@AllArgsConstructor
public class ImplB extends Abstract{

    private String path;

    private Integer quantity;
}
