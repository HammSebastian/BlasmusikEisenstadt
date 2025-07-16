package com.hammsebastian.backend_stadtkapelle_eisenstadt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BackendStadtkapelleEisenstadtApplicationTests {

	@Test
	void contextLoads() {
	}

}
