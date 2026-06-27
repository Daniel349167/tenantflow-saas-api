package dev.daniel.tenantflow.domain;

public class InvalidStateTransitionException extends RuntimeException {

    public InvalidStateTransitionException(WorkOrderStatus current, WorkOrderStatus target) {
        super("Cannot transition a work order from " + current + " to " + target);
    }
}
