package com.test.rsqlattrconverterscenario.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.rsqlattrconverterscenario.data.ModelRepository;
import com.test.rsqlattrconverterscenario.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
}
