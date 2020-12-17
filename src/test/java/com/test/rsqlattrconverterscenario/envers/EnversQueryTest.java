package com.test.rsqlattrconverterscenario.envers;

import com.test.rsqlattrconverterscenario.data.ModelRepository;
import com.test.rsqlattrconverterscenario.model.ModelA;
import com.test.rsqlattrconverterscenario.model.ModelD;
import org.assertj.core.api.Assertions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestEntityManager
public class EnversQueryTest {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private AuditReader auditReader;

    @BeforeEach
    public void setup() {
        auditReader = AuditReaderFactory.get(testEntityManager.getEntityManager());
    }

    @Test
    public void newModel_queryModelAId_getsTimeOfLatestRevision() {
        ModelA savedModel = modelRepository.save(new ModelA());
        testEntityManager.getEntityManager().getTransaction().commit();
        testEntityManager.getEntityManager().getTransaction().begin();
        savedModel.getKeyToModelD().put("SOME_KEY", new ModelD());
        modelRepository.save(savedModel);
        testEntityManager.getEntityManager().getTransaction().commit();
        List<Number> revisionsOrderByIdAsc = auditReader.getRevisions(ModelA.class, savedModel.getId());
        Date lastRevisionDate = auditReader.getRevisionDate(revisionsOrderByIdAsc.get(revisionsOrderByIdAsc.size()-1));
        Assertions.assertThat(lastRevisionDate).isNotNull();
        Assertions.assertThat(lastRevisionDate.before(new Date())).isTrue();
    }

    @Test
    public void newModel_queryModelABySavedBeforeACertainDate_finds() throws Exception {
        ModelA savedModel1 = modelRepository.save(new ModelA());
        testEntityManager.getEntityManager().getTransaction().commit();
        Thread.sleep(3000);
        testEntityManager.getEntityManager().getTransaction().begin();
        Date model2SavedDate = new Date();
        modelRepository.save(new ModelA());
        testEntityManager.getEntityManager().getTransaction().commit();
        ModelA foundModel = (ModelA) auditReader
                .createQuery()
                .forRevisionsOfEntity(ModelA.class, true, false)
                .add(AuditEntity.revisionType().eq(RevisionType.ADD))
                .add(AuditEntity.revisionProperty("timestamp").lt(model2SavedDate.getTime()))
                .getSingleResult();
        Assertions.assertThat(foundModel.getId()).isEqualTo(savedModel1.getId());
    }

    @Test
    public void newModel_queryModelABySavedAfterACertainDate_finds() throws Exception {
        modelRepository.save(new ModelA());
        testEntityManager.getEntityManager().getTransaction().commit();
        Thread.sleep(3000);
        testEntityManager.getEntityManager().getTransaction().begin();
        Date model2SavedDate = new Date();
        ModelA savedModel2 = modelRepository.save(new ModelA());
        testEntityManager.getEntityManager().getTransaction().commit();
        ModelA foundModel = (ModelA) auditReader
                .createQuery()
                .forRevisionsOfEntity(ModelA.class, true, false)
                .add(AuditEntity.revisionType().eq(RevisionType.ADD))
                .add(AuditEntity.revisionProperty("timestamp").gt(model2SavedDate.getTime()))
                .getSingleResult();
        Assertions.assertThat(foundModel.getId()).isEqualTo(savedModel2.getId());
    }
}
