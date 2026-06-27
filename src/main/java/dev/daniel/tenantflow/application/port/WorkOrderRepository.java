package dev.daniel.tenantflow.application.port;

import dev.daniel.tenantflow.domain.WorkOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkOrderRepository {

    WorkOrder save(WorkOrder workOrder);

    Optional<WorkOrder> findByIdAndTenantId(UUID id, String tenantId);

    List<WorkOrder> findAllByTenantId(String tenantId);
}
