package com.test.rsqlattrconverterscenario.model;

import com.test.rsqlattrconverterscenario.custom.CustomString;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Audited
public class ModelC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final CustomString customString = CustomString.fromString("SOME_STRING");
}
