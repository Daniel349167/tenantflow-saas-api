package dev.daniel.tenantflow.api;

import dev.daniel.tenantflow.application.CreateWorkOrderCommand;
import dev.daniel.tenantflow.application.WorkOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/work-orders")
@SecurityRequirement(name = "bearerAuth")
public class WorkOrderController {

    private final WorkOrderService service;

    public WorkOrderController(WorkOrderService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('WORK_ORDER_WRITE')")
    public ResponseEntity<WorkOrderResponse> create(@Valid @RequestBody CreateWorkOrderRequest request) {
        var created = service.create(new CreateWorkOrderCommand(
                request.title(), request.description(), request.priority(),
                request.assigneeEmail(), request.dueAt()));
        return ResponseEntity.created(URI.create("/api/v1/work-orders/" + created.id()))
                .body(WorkOrderResponse.from(created));
    }

    @GetMapping
    @PreAuthorize("hasRole('WORK_ORDER_READ')")
    public List<WorkOrderResponse> findAll() {
        return service.findAll().stream().map(WorkOrderResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('WORK_ORDER_READ')")
    public WorkOrderResponse find(@PathVariable UUID id) {
        return WorkOrderResponse.from(service.find(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('WORK_ORDER_WRITE')")
    public WorkOrderResponse changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeStatusRequest request) {
        return WorkOrderResponse.from(service.changeStatus(id, request.status()));
    }
}
