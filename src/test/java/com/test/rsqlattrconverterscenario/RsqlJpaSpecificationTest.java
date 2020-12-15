package com.test.rsqlattrconverterscenario;

import com.test.rsqlattrconverterscenario.data.ModelRepository;
import com.test.rsqlattrconverterscenario.model.ModelA;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DataJpaTest
@AutoConfigureTestEntityManager
public class RsqlJpaSpecificationTest {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void newModelA_findByModelBId() {
        ModelA savedModel = modelRepository.save(new ModelA());
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("modelB.id==" + savedModel.getId());
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByCustomStringValueWithValue() {
        ModelA savedModel = modelRepository.save(new ModelA());
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("modelB.modelC.customString.value==SOME_STRING");
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByCustomStringValueWithoutValue() {
        ModelA savedModel = modelRepository.save(new ModelA());
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("modelB.modelC.customString==SOME_STRING");
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    private RSQLJPASupport getRsqljpaSupport(TestEntityManager testEntityManager) {
        Map<String, EntityManager> emToEntityManager = new HashMap<>();
        emToEntityManager.put("em", testEntityManager.getEntityManager());
        return new RSQLJPASupport(emToEntityManager);
    }
}
