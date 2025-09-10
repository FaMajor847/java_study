package org.mav.example.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FlywayReset {
    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }

}
