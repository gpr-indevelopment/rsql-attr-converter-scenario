package com.test.rsqlattrconverterscenario.data;

import com.test.rsqlattrconverterscenario.model.ModelA;
import com.test.rsqlattrconverterscenario.model.ModelD;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class DbPopulator {

    private final ModelRepository modelRepository;

    @PostConstruct
    public void populate() {
        ModelA modelA = new ModelA();
        modelA.getKeyToModelD().put("SOME_KEY", new ModelD());
        modelRepository.save(modelA);
    }
}
