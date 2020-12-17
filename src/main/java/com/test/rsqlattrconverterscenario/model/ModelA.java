package com.test.rsqlattrconverterscenario.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@Audited
public class ModelA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "MODEL_A_ID")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "key")
    Map<String, ModelD> keyToModelD = new HashMap<>();

    @JoinColumn(name = "MODEL_A_ID")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "key")
    Map<String, Abstract> keyToAbstract = new HashMap<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MODEL_B_ID", referencedColumnName = "id")
    private final ModelB modelB = new ModelB();
}
