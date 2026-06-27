package dev.daniel.tenantflow.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface SpringDataWorkOrderRepository extends JpaRepository<WorkOrderJpaEntity, UUID> {

    Optional<WorkOrderJpaEntity> findByIdAndTenantId(UUID id, String tenantId);

    List<WorkOrderJpaEntity> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);
}
