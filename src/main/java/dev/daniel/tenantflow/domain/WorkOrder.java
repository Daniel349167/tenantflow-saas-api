package dev.daniel.tenantflow.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class WorkOrder {

    private final UUID id;
    private final String tenantId;
    private final String title;
    private final String description;
    private final WorkOrderPriority priority;
    private final WorkOrderStatus status;
    private final String assigneeEmail;
    private final Instant dueAt;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final long version;

    private WorkOrder(
            UUID id,
            String tenantId,
            String title,
            String description,
            WorkOrderPriority priority,
            WorkOrderStatus status,
            String assigneeEmail,
            Instant dueAt,
            Instant createdAt,
            Instant updatedAt,
            long version) {
        this.id = Objects.requireNonNull(id);
        this.tenantId = requireText(tenantId, "tenantId");
        this.title = requireText(title, "title");
        this.description = description;
        this.priority = Objects.requireNonNull(priority);
        this.status = Objects.requireNonNull(status);
        this.assigneeEmail = assigneeEmail;
        this.dueAt = dueAt;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.version = version;
    }

    public static WorkOrder create(
            String tenantId,
            String title,
            String description,
            WorkOrderPriority priority,
            String assigneeEmail,
            Instant dueAt,
            Instant now) {
        if (dueAt != null && dueAt.isBefore(now)) {
            throw new IllegalArgumentException("Due date must not be in the past");
        }
        return new WorkOrder(
                UUID.randomUUID(), tenantId, title, description, priority,
                WorkOrderStatus.OPEN, assigneeEmail, dueAt, now, now, 0);
    }

    public static WorkOrder rehydrate(
            UUID id,
            String tenantId,
            String title,
            String description,
            WorkOrderPriority priority,
            WorkOrderStatus status,
            String assigneeEmail,
            Instant dueAt,
            Instant createdAt,
            Instant updatedAt,
            long version) {
        return new WorkOrder(
                id, tenantId, title, description, priority, status,
                assigneeEmail, dueAt, createdAt, updatedAt, version);
    }

    public WorkOrder transitionTo(WorkOrderStatus target, Instant now) {
        boolean allowed = switch (status) {
            case OPEN -> target == WorkOrderStatus.IN_PROGRESS || target == WorkOrderStatus.CANCELLED;
            case IN_PROGRESS -> target == WorkOrderStatus.COMPLETED || target == WorkOrderStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
        if (!allowed) {
            throw new InvalidStateTransitionException(status, target);
        }
        return new WorkOrder(
                id, tenantId, title, description, priority, target,
                assigneeEmail, dueAt, createdAt, now, version);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String title() { return title; }
    public String description() { return description; }
    public WorkOrderPriority priority() { return priority; }
    public WorkOrderStatus status() { return status; }
    public String assigneeEmail() { return assigneeEmail; }
    public Instant dueAt() { return dueAt; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
    public long version() { return version; }
}
