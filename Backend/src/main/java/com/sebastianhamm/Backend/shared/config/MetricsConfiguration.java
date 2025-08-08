package com.sebastianhamm.Backend.shared.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configuration for custom business metrics and Micrometer enhancements.
 * Provides application-specific metrics for monitoring business operations.
 */
@Configuration
public class MetricsConfiguration {

    // Business metrics counters
    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final AtomicInteger totalEvents = new AtomicInteger(0);
    private final AtomicInteger totalGalleryViews = new AtomicInteger(0);
    private final AtomicInteger totalImageUploads = new AtomicInteger(0);

    /**
     * Customizes the MeterRegistry with application-specific tags and configuration.
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(AppConfiguration appConfig) {
        return registry -> {
            // Add common tags to all metrics
            registry.config()
                    .commonTags(
                            "application", "stadtkapelle-backend",
                            "environment", appConfig.environment() != null ? appConfig.environment() : "unknown",
                            "version", getClass().getPackage().getImplementationVersion() != null 
                                    ? getClass().getPackage().getImplementationVersion() 
                                    : "dev"
                    );
        };
    }

    /**
     * Creates custom business metrics beans.
     */
    @Bean
    public Counter authenticationSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("auth.success")
                .description("Number of successful authentications")
                .tag("type", "jwt")
                .register(meterRegistry);
    }

    @Bean
    public Counter authenticationFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("auth.failure")
                .description("Number of failed authentications")
                .tag("type", "jwt")
                .register(meterRegistry);
    }

    @Bean
    public Counter eventViewCounter(MeterRegistry meterRegistry) {
        return Counter.builder("business.events.views")
                .description("Number of event views")
                .register(meterRegistry);
    }

    @Bean
    public Counter galleryViewCounter(MeterRegistry meterRegistry) {
        return Counter.builder("business.gallery.views")
                .description("Number of gallery views")
                .register(meterRegistry);
    }

    @Bean
    public Counter imageUploadCounter(MeterRegistry meterRegistry) {
        return Counter.builder("business.images.uploads")
                .description("Number of image uploads")
                .register(meterRegistry);
    }

    @Bean
    public Timer databaseQueryTimer(MeterRegistry meterRegistry) {
        return Timer.builder("database.query.duration")
                .description("Database query execution time")
                .register(meterRegistry);
    }

    @Bean
    public Timer fileUploadTimer(MeterRegistry meterRegistry) {
        return Timer.builder("file.upload.duration")
                .description("File upload processing time")
                .register(meterRegistry);
    }

    /**
     * Gauge metrics for current state monitoring.
     */
    @Bean
    public Gauge activeUsersGauge(MeterRegistry meterRegistry) {
        return Gauge.builder("business.users.active", activeUsers, AtomicInteger::get)
                .description("Number of currently active users")
                .register(meterRegistry);
    }

    @Bean
    public Gauge totalEventsGauge(MeterRegistry meterRegistry) {
        return Gauge.builder("business.events.total", totalEvents, AtomicInteger::get)
                .description("Total number of events in the system")
                .register(meterRegistry);
    }

    @Bean
    public Gauge totalGalleryViewsGauge(MeterRegistry meterRegistry) {
        return Gauge.builder("business.gallery.total_views", totalGalleryViews, AtomicInteger::get)
                .description("Total number of gallery views")
                .register(meterRegistry);
    }

    @Bean
    public Gauge totalImageUploadsGauge(MeterRegistry meterRegistry) {
        return Gauge.builder("business.images.total_uploads", totalImageUploads, AtomicInteger::get)
                .description("Total number of image uploads")
                .register(meterRegistry);
    }

    // Utility methods to update gauge values
    public void incrementActiveUsers() {
        activeUsers.incrementAndGet();
    }

    public void decrementActiveUsers() {
        activeUsers.decrementAndGet();
    }

    public void setTotalEvents(int count) {
        totalEvents.set(count);
    }

    public void incrementTotalGalleryViews() {
        totalGalleryViews.incrementAndGet();
    }

    public void incrementTotalImageUploads() {
        totalImageUploads.incrementAndGet();
    }
}