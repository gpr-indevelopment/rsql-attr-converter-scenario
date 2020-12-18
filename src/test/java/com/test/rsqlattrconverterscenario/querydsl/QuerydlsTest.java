package com.test.rsqlattrconverterscenario.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.rsqlattrconverterscenario.data.ModelRepository;
import com.test.rsqlattrconverterscenario.model.*;
import io.github.perplexhub.rsql.RSQLQueryDslSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestEntityManager
public class QuerydlsTest {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void setup() {
        queryFactory = new JPAQueryFactory(testEntityManager.getEntityManager());
    }

    @Test
    public void pureQueryDsl_findById_finds() {
        // given
        ModelA savedModel = modelRepository.save(new ModelA());
        QModelA qModelA = QModelA.modelA;
        // then
        ModelA foundModel = queryFactory.selectFrom(qModelA).where(qModelA.id.eq(savedModel.getId())).fetchOne();
        assertThat(foundModel.getId()).isEqualTo(savedModel.getId());
    }

    // Cant be done with QAbstract since it does not have the path field.
    @Test
    public void pureQueryDsl_findByImplAPathOnQImplA_finds() {
        // given
        ModelA modelA = new ModelA();
        modelA.getKeyToAbstract().put("SOME_KEY", new ImplA(true, "SOME_PATH"));
        ModelA savedModel = modelRepository.save(modelA);
        QImplA qImplA = QImplA.implA;
        // then
        ImplA foundModel = queryFactory.selectFrom(qImplA).where(qImplA.path.eq("SOME_PATH")).fetchOne();
        assertThat(foundModel.getId()).isEqualTo(savedModel.getKeyToAbstract().get("SOME_KEY").getId());
    }

    @Test
    public void pureQueryDsl_findByWrongImplAPathOnQImplA_findsNothing() {
        // given
        ModelA modelA = new ModelA();
        modelA.getKeyToAbstract().put("SOME_KEY", new ImplA(true, "SOME_PATH"));
        modelRepository.save(modelA);
        QImplA qImplA = QImplA.implA;
        // then
        ImplA foundModel = queryFactory.selectFrom(qImplA).where(qImplA.path.eq("SOME_PATH_THAT_DOESNT_EXIST")).fetchOne();
        assertThat(foundModel).isNull();
    }

    @Test
    public void queryDslAndRsql_findByImplAPathOnQImplA_finds() {
        // given
        ModelA modelA = new ModelA();
        modelA.getKeyToAbstract().put("SOME_KEY", new ImplA(true, "SOME_PATH"));
        ModelA savedModel = modelRepository.save(modelA);
        // then
        Map<String, EntityManager> stringToEm = new HashMap<>();
        stringToEm.put("em", testEntityManager.getEntityManager());
        RSQLQueryDslSupport rsqlQueryDslSupport = new RSQLQueryDslSupport(stringToEm);
        BooleanExpression booleanExpression = rsqlQueryDslSupport.toPredicate("keyToAbstract.path==SOME_PATH", QModelA.modelA);
        modelRepository.findAll(booleanExpression).forEach(model -> assertThat(model.getId()).isEqualTo(savedModel.getKeyToAbstract().get("SOME_KEY").getId()));
    }
}
