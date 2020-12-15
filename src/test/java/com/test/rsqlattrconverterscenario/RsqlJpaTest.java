package com.test.rsqlattrconverterscenario;

import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;
import com.test.rsqlattrconverterscenario.data.ModelRepository;
import com.test.rsqlattrconverterscenario.model.ModelA;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@DataJpaTest
@AutoConfigureTestEntityManager
public class RsqlJpaTest {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void newModelA_findByModelBId() {
        ModelA savedModel = modelRepository.save(new ModelA());
        Node rootNode = new RSQLParser().parse("modelB.id==" + savedModel.getId());
        RSQLVisitor<CriteriaQuery<ModelA>, EntityManager> visitor = new JpaCriteriaQueryVisitor<>();
        CriteriaQuery<ModelA> query = rootNode.accept(visitor, testEntityManager.getEntityManager());
        List<ModelA> foundEntities = testEntityManager.getEntityManager().createQuery(query).getResultList();
        Assertions.assertThat(foundEntities.size()).isEqualTo(1);
        Assertions.assertThat(foundEntities.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByCustomStringValueWithValue() {
        ModelA savedModel = modelRepository.save(new ModelA());
        Node rootNode = new RSQLParser().parse("modelB.modelC.customString.value==SOME_STRING");
        RSQLVisitor<CriteriaQuery<ModelA>, EntityManager> visitor = new JpaCriteriaQueryVisitor<>();
        CriteriaQuery<ModelA> query = rootNode.accept(visitor, testEntityManager.getEntityManager());
        List<ModelA> foundEntities = testEntityManager.getEntityManager().createQuery(query).getResultList();
        Assertions.assertThat(foundEntities.size()).isEqualTo(1);
        Assertions.assertThat(foundEntities.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }

    @Test
    public void newModelA_findsByCustomStringValueWithoutValue() {
        ModelA savedModel = modelRepository.save(new ModelA());
        Node rootNode = new RSQLParser().parse("modelB.modelC.customString==SOME_STRING");
        RSQLVisitor<CriteriaQuery<ModelA>, EntityManager> visitor = new JpaCriteriaQueryVisitor<>();
        CriteriaQuery<ModelA> query = rootNode.accept(visitor, testEntityManager.getEntityManager());
        List<ModelA> foundEntities = testEntityManager.getEntityManager().createQuery(query).getResultList();
        Assertions.assertThat(foundEntities.size()).isEqualTo(1);
        Assertions.assertThat(foundEntities.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }
}
