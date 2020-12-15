package com.test.rsqlattrconverterscenario.data;

import com.test.rsqlattrconverterscenario.model.ModelA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModelRepository extends JpaRepository<ModelA, Long>, JpaSpecificationExecutor<ModelA> {
}
