package com.test.rsqlattrconverterscenario.rsql;

import com.test.rsqlattrconverterscenario.custom.StringToCustomStringConverter;
import com.test.rsqlattrconverterscenario.data.ModelRepository;
import com.test.rsqlattrconverterscenario.model.ImplA;
import com.test.rsqlattrconverterscenario.model.ModelA;
import com.test.rsqlattrconverterscenario.model.ModelD;
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
    public void newModelA_findsByCustomStringValueWithoutValue() {
        ModelA savedModel = modelRepository.save(new ModelA());
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("modelB.modelC.customString==SOME_STRING");
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByKeyToModelDWithModelDId_find() {
        ModelA modelA = new ModelA();
        modelA.getKeyToModelD().put("SOME_KEY", new ModelD("SOME_KEY"));
        ModelA savedModel = modelRepository.save(modelA);
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("keyToModelD.id==1");
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByKeyToModelDByKey_find() {
        ModelA modelA = new ModelA();
        modelA.getKeyToModelD().put("SOME_KEY", new ModelD("SOME_KEY"));
        ModelA savedModel = modelRepository.save(modelA);
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("keyToModelD.key==SOME_KEY");
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByKeyToModelDByKeyThatDoesntExist_findsNothing() {
        ModelA modelA = new ModelA();
        modelA.getKeyToModelD().put("SOME_KEY", new ModelD("SOME_KEY"));
        modelRepository.save(modelA);
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("keyToModelD.key==SOME_KEY_THAT_DOES_NOT_EXIST");
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(0);
    }

    @Test
    public void newModelA_findsByKeyToAbstractById_finds() {
        ModelA modelA = new ModelA();
        modelA.getKeyToAbstract().put("SOME_KEY", new ImplA(true, "SOME_PATH"));
        ModelA savedModel = modelRepository.save(modelA);
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("keyToAbstract.id==" + savedModel.getKeyToAbstract().get("SOME_KEY").getId());
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByKeyToAbstractByImplAIsAlive_finds() {
        ModelA modelA = new ModelA();
        modelA.getKeyToAbstract().put("SOME_KEY", new ImplA(true, "SOME_PATH"));
        ModelA savedModel = modelRepository.save(modelA);
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("keyToAbstract.isAlive==true");
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByAbstractsPath_finds() {
        ModelA modelA = new ModelA();
        modelA.getAbstracts().add(new ImplA(true, "SOME_PATH"));
        ModelA savedModel = modelRepository.save(modelA);
        Specification<ModelA> spec = getRsqljpaSupport(testEntityManager).toSpecification("abstracts.isAlive==true");
        List<ModelA> foundModels = modelRepository.findAll(spec);
        Assertions.assertThat(foundModels).hasSize(1);
        Assertions.assertThat(foundModels.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    private RSQLJPASupport getRsqljpaSupport(TestEntityManager testEntityManager) {
        Map<String, EntityManager> emToEntityManager = new HashMap<>();
        emToEntityManager.put("em", testEntityManager.getEntityManager());
        RSQLJPASupport.addConverter(new StringToCustomStringConverter());
        return new RSQLJPASupport(emToEntityManager);
    }
}
