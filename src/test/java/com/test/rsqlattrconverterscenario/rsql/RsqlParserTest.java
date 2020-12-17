package com.test.rsqlattrconverterscenario.rsql;

import com.test.rsqlattrconverterscenario.data.ModelRepository;
import com.test.rsqlattrconverterscenario.model.ModelA;
import com.test.rsqlattrconverterscenario.rsqlparser.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@DataJpaTest
public class RsqlParserTest {

    @Autowired
    private ModelRepository modelRepository;

    @Test
    public void newModelA_findByModelBId() {
        ModelA savedModel = modelRepository.save(new ModelA());
        Node rootNode = new RSQLParser().parse("modelB.id==" + savedModel.getId());
        Specification<ModelA> spec = rootNode.accept(new CustomRsqlVisitor<>());
        List<ModelA> foundEntities = modelRepository.findAll(spec);
        Assertions.assertThat(foundEntities.size()).isEqualTo(1);
        Assertions.assertThat(foundEntities.get(0)).usingRecursiveComparison().isEqualTo(savedModel);
    }
}
