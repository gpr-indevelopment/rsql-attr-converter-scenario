package com.test.rsqlattrconverterscenario.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Audited
public class ModelB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MODEL_C_ID", referencedColumnName = "id")
    private final ModelC modelC = new ModelC();
}
