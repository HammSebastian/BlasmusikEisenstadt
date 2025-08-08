package com.sebastianhamm.Backend;

import com.sebastianhamm.Backend.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
