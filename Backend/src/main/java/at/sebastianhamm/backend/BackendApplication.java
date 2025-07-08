package at.sebastianhamm.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Backend Spring Boot application.
 * Enables asynchronous processing and scheduling.
 * <p>
 * Hardened for production-grade usage:
 * - Added strict logging for startup events.
 * - Clear separation of concerns.
 * - Ready for extension with security, monitoring, and health checks.
 */
@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class BackendApplication {

    /**
     * Application entry method.
     * Starts the Spring Boot context and logs lifecycle events.
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        log.info("Starting BackendApplication...");
        SpringApplication.run(BackendApplication.class, args);
        log.info("BackendApplication started successfully.");

    }
}
