package dev.daniel.tenantflow.application;

import dev.daniel.tenantflow.application.port.TenantContext;
import dev.daniel.tenantflow.application.port.WorkOrderRepository;
import dev.daniel.tenantflow.domain.WorkOrder;
import dev.daniel.tenantflow.domain.WorkOrderPriority;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkOrderServiceTest {

    private static final Instant NOW = Instant.parse("2026-06-27T12:00:00Z");

    @Test
    void preventsCrossTenantReads() {
        var repository = new InMemoryRepository();
        var tenant = new MutableTenantContext("acme");
        var service = new WorkOrderService(
                repository, tenant, Clock.fixed(NOW, ZoneOffset.UTC));

        var created = service.create(new CreateWorkOrderCommand(
                "Acme alert", null, WorkOrderPriority.CRITICAL, null, null));

        tenant.tenantId = "globex";

        assertThrows(WorkOrderNotFoundException.class, () -> service.find(created.id()));
        assertEquals(List.of(), service.findAll());
    }

    private static final class MutableTenantContext implements TenantContext {
        private String tenantId;

        private MutableTenantContext(String tenantId) {
            this.tenantId = tenantId;
        }

        @Override
        public String requiredTenantId() {
            return tenantId;
        }
    }

    private static final class InMemoryRepository implements WorkOrderRepository {
        private final Map<UUID, WorkOrder> data = new LinkedHashMap<>();

        @Override
        public WorkOrder save(WorkOrder workOrder) {
            data.put(workOrder.id(), workOrder);
            return workOrder;
        }

        @Override
        public Optional<WorkOrder> findByIdAndTenantId(UUID id, String tenantId) {
            return Optional.ofNullable(data.get(id))
                    .filter(workOrder -> workOrder.tenantId().equals(tenantId));
        }

        @Override
        public List<WorkOrder> findAllByTenantId(String tenantId) {
            var result = new ArrayList<WorkOrder>();
            data.values().stream()
                    .filter(workOrder -> workOrder.tenantId().equals(tenantId))
                    .forEach(result::add);
            return result;
        }
    }
}
