package dev.daniel.tenantflow.infrastructure;

import dev.daniel.tenantflow.domain.WorkOrder;
import dev.daniel.tenantflow.domain.WorkOrderPriority;
import dev.daniel.tenantflow.infrastructure.persistence.JpaWorkOrderRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaWorkOrderRepositoryAdapter.class)
class JpaWorkOrderRepositoryAdapterTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    JpaWorkOrderRepositoryAdapter repository;

    @Test
    void persistsAndScopesWorkOrdersByTenant() {
        Instant now = Instant.parse("2026-06-27T12:00:00Z");
        var workOrder = WorkOrder.create(
                "acme", "Reconcile settlement", null,
                WorkOrderPriority.HIGH, null, null, now);

        var saved = repository.save(workOrder);

        assertTrue(repository.findByIdAndTenantId(saved.id(), "acme").isPresent());
        assertTrue(repository.findByIdAndTenantId(saved.id(), "globex").isEmpty());
        assertEquals(1, repository.findAllByTenantId("acme").size());
    }
}
