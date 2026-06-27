package dev.daniel.tenantflow.infrastructure.persistence;

import dev.daniel.tenantflow.domain.WorkOrder;
import dev.daniel.tenantflow.domain.WorkOrderPriority;
import dev.daniel.tenantflow.domain.WorkOrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "work_orders")
class WorkOrderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkOrderPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkOrderStatus status;

    @Column(name = "assignee_email")
    private String assigneeEmail;

    @Column(name = "due_at")
    private Instant dueAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    protected WorkOrderJpaEntity() {
    }

    static WorkOrderJpaEntity from(WorkOrder workOrder) {
        var entity = new WorkOrderJpaEntity();
        entity.id = workOrder.id();
        entity.apply(workOrder);
        return entity;
    }

    void apply(WorkOrder workOrder) {
        this.tenantId = workOrder.tenantId();
        this.title = workOrder.title();
        this.description = workOrder.description();
        this.priority = workOrder.priority();
        this.status = workOrder.status();
        this.assigneeEmail = workOrder.assigneeEmail();
        this.dueAt = workOrder.dueAt();
        this.createdAt = workOrder.createdAt();
        this.updatedAt = workOrder.updatedAt();
    }

    WorkOrder toDomain() {
        return WorkOrder.rehydrate(
                id, tenantId, title, description, priority, status, assigneeEmail,
                dueAt, createdAt, updatedAt, version == null ? 0 : version);
    }
}
