package com.test.rsqlattrconverterscenario.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ModelB {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MODEL_C_ID", referencedColumnName = "id")
    private final ModelC modelC = new ModelC();
}
