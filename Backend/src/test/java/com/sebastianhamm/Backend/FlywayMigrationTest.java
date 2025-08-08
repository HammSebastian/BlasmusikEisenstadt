/**
 * Copyright (c) 2025 Sebastian Hamm. All Rights Reserved.
 *
 * @author Sebastian Hamm
 * @version 1.0.0
 * @since 8/4/25
 */
package com.sebastianhamm.Backend;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FlywayMigrationTest {

    private DataSource dataSource;
    private Flyway flyway;

    @BeforeEach
    public void setUp() {
        System.out.println("[DEBUG_LOG] Setting up test database");
        
        // Create H2 in-memory database
        JdbcDataSource h2DataSource = new JdbcDataSource();
        h2DataSource.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        h2DataSource.setUser("sa");
        h2DataSource.setPassword("");
        
        this.dataSource = h2DataSource;
        
        // Configure Flyway
        FluentConfiguration config = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true);
        
        this.flyway = config.load();
    }

    @AfterEach
    public void tearDown() {
        // No cleanup needed for in-memory database
        System.out.println("[DEBUG_LOG] Test completed successfully");
    }

    @Test
    public void testFlywayMigrationExecutesSuccessfully() {
        System.out.println("[DEBUG_LOG] Testing Flyway migration execution");
        
        // Execute migrations
        var migrationResult = flyway.migrate();
        System.out.println("[DEBUG_LOG] Migrations executed: " + migrationResult.migrationsExecuted);
        
        // Verify migrations were applied
        var migrationInfo = flyway.info();
        assertThat(migrationInfo.all()).isNotEmpty();
        
        System.out.println("[DEBUG_LOG] Number of migrations: " + migrationInfo.all().length);
        
        // Verify that the main tables are created
        verifyTableExists("about");
        verifyTableExists("events");
        verifyTableExists("locations");
        verifyTableExists("members");
        verifyTableExists("welcome");
        verifyTableExists("gallery");
        verifyTableExists("images");
        
        // Verify audit tables are created
        verifyTableExists("about_aud");
        verifyTableExists("events_aud");
        verifyTableExists("locations_aud");
        verifyTableExists("members_aud");
        verifyTableExists("revinfo");
        
        System.out.println("[DEBUG_LOG] All expected tables exist");
    }

    private void verifyTableExists(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE UPPER(table_name) = UPPER(?)";
            try (var statement = connection.prepareStatement(sql)) {
                statement.setString(1, tableName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        assertThat(count).isEqualTo(1);
                        System.out.println("[DEBUG_LOG] Table '" + tableName + "' exists");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to verify table existence: " + tableName, e);
        }
    }
}