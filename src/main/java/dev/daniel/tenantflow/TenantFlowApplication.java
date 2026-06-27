package dev.daniel.tenantflow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "TenantFlow API",
        version = "1.0.0",
        description = "Tenant-isolated work-order management API"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class TenantFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantFlowApplication.class, args);
    }

    @Bean
    Clock systemClock() {
        return Clock.systemUTC();
    }
}
