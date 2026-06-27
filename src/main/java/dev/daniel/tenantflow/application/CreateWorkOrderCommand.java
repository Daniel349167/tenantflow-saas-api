package dev.daniel.tenantflow.application;

import dev.daniel.tenantflow.domain.WorkOrderPriority;

import java.time.Instant;

public record CreateWorkOrderCommand(
        String title,
        String description,
        WorkOrderPriority priority,
        String assigneeEmail,
        Instant dueAt) {
}
