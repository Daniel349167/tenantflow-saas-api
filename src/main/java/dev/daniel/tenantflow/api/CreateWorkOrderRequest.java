package dev.daniel.tenantflow.api;

import dev.daniel.tenantflow.domain.WorkOrderPriority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateWorkOrderRequest(
        @NotBlank @Size(max = 160) String title,
        @Size(max = 4000) String description,
        @NotNull WorkOrderPriority priority,
        @Email @Size(max = 254) String assigneeEmail,
        @Future Instant dueAt) {
}
