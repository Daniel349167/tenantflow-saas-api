package dev.daniel.tenantflow.application;

import dev.daniel.tenantflow.application.port.TenantContext;
import dev.daniel.tenantflow.application.port.WorkOrderRepository;
import dev.daniel.tenantflow.domain.WorkOrder;
import dev.daniel.tenantflow.domain.WorkOrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class WorkOrderService {

    private final WorkOrderRepository repository;
    private final TenantContext tenantContext;
    private final Clock clock;

    public WorkOrderService(WorkOrderRepository repository, TenantContext tenantContext, Clock clock) {
        this.repository = repository;
        this.tenantContext = tenantContext;
        this.clock = clock;
    }

    @Transactional
    public WorkOrder create(CreateWorkOrderCommand command) {
        String tenantId = tenantContext.requiredTenantId();
        Instant now = clock.instant();
        var workOrder = WorkOrder.create(
                tenantId,
                command.title(),
                command.description(),
                command.priority(),
                command.assigneeEmail(),
                command.dueAt(),
                now);
        return repository.save(workOrder);
    }

    @Transactional(readOnly = true)
    public WorkOrder find(UUID id) {
        String tenantId = tenantContext.requiredTenantId();
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new WorkOrderNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<WorkOrder> findAll() {
        return repository.findAllByTenantId(tenantContext.requiredTenantId());
    }

    @Transactional
    public WorkOrder changeStatus(UUID id, WorkOrderStatus status) {
        String tenantId = tenantContext.requiredTenantId();
        var current = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new WorkOrderNotFoundException(id));
        return repository.save(current.transitionTo(status, clock.instant()));
    }
}
