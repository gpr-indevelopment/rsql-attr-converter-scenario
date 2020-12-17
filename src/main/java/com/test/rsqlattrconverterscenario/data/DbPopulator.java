package com.test.rsqlattrconverterscenario.data;

import com.test.rsqlattrconverterscenario.model.ImplA;
import com.test.rsqlattrconverterscenario.model.ImplB;
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
        modelA.getKeyToModelD().put("SOME_KEY", new ModelD("SOME_KEY"));
        modelA.getKeyToAbstract().put("IMPLA", new ImplA(true, "SOME_PATH"));
        modelA.getKeyToAbstract().put("IMPLB", new ImplB("OTHER_PATH", 2));
        modelRepository.save(modelA);
    }
}
