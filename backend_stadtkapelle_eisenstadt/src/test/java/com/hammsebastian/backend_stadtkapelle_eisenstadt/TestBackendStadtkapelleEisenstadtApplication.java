package com.hammsebastian.backend_stadtkapelle_eisenstadt;

import org.springframework.boot.SpringApplication;

public class TestBackendStadtkapelleEisenstadtApplication {

	public static void main(String[] args) {
		SpringApplication.from(BackendStadtkapelleEisenstadtApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
