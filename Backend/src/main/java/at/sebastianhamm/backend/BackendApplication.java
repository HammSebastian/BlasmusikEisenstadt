package at.sebastianhamm.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		log.info("Starting BackendApplication");
		SpringApplication.run(BackendApplication.class, args);
		log.info("BackendApplication started");
	}

}
