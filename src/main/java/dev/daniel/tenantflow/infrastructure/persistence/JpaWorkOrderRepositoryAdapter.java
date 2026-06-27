package dev.daniel.tenantflow.infrastructure.persistence;

import dev.daniel.tenantflow.application.port.WorkOrderRepository;
import dev.daniel.tenantflow.domain.WorkOrder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaWorkOrderRepositoryAdapter implements WorkOrderRepository {

    private final SpringDataWorkOrderRepository repository;

    public JpaWorkOrderRepositoryAdapter(SpringDataWorkOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public WorkOrder save(WorkOrder workOrder) {
        var entity = repository.findByIdAndTenantId(workOrder.id(), workOrder.tenantId())
                .orElseGet(() -> WorkOrderJpaEntity.from(workOrder));
        entity.apply(workOrder);
        return repository.saveAndFlush(entity).toDomain();
    }

    @Override
    public Optional<WorkOrder> findByIdAndTenantId(UUID id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).map(WorkOrderJpaEntity::toDomain);
    }

    @Override
    public List<WorkOrder> findAllByTenantId(String tenantId) {
        return repository.findAllByTenantIdOrderByCreatedAtDesc(tenantId).stream()
                .map(WorkOrderJpaEntity::toDomain)
                .toList();
    }
}
