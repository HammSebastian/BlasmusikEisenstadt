package com.hammsebastian.backend_stadtkapelle_eisenstadt;

import com.hammsebastian.backend_stadtkapelle_eisenstadt.configuration.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
@EnableJpaAuditing
public class BackendStadtkapelleEisenstadtApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendStadtkapelleEisenstadtApplication.class, args);
	}
}
