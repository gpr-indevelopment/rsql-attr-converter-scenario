package com.test.rsqlattrconverterscenario.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ModelA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MODEL_B_ID", referencedColumnName = "id")
    private final ModelB modelB = new ModelB();
}
