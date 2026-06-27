package dev.daniel.tenantflow.api;

import dev.daniel.tenantflow.domain.WorkOrderStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(@NotNull WorkOrderStatus status) {
}
