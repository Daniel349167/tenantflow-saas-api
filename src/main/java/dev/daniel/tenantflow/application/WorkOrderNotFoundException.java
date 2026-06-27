package dev.daniel.tenantflow.application;

import java.util.UUID;

public class WorkOrderNotFoundException extends RuntimeException {

    public WorkOrderNotFoundException(UUID id) {
        super("Work order not found: " + id);
    }
}
