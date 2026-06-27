package dev.daniel.tenantflow.api;

import dev.daniel.tenantflow.domain.WorkOrder;
import dev.daniel.tenantflow.domain.WorkOrderPriority;
import dev.daniel.tenantflow.domain.WorkOrderStatus;

import java.time.Instant;
import java.util.UUID;

public record WorkOrderResponse(
        UUID id,
        String title,
        String description,
        WorkOrderPriority priority,
        WorkOrderStatus status,
        String assigneeEmail,
        Instant dueAt,
        Instant createdAt,
        Instant updatedAt,
        long version) {

    static WorkOrderResponse from(WorkOrder workOrder) {
        return new WorkOrderResponse(
                workOrder.id(), workOrder.title(), workOrder.description(),
                workOrder.priority(), workOrder.status(), workOrder.assigneeEmail(),
                workOrder.dueAt(), workOrder.createdAt(), workOrder.updatedAt(),
                workOrder.version());
    }
}
