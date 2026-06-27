package dev.daniel.tenantflow.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkOrderTest {

    private static final Instant NOW = Instant.parse("2026-06-27T12:00:00Z");

    @Test
    void followsTheAllowedLifecycle() {
        var open = WorkOrder.create(
                "acme", "Investigate alert", null, WorkOrderPriority.HIGH,
                null, NOW.plusSeconds(3600), NOW);

        var inProgress = open.transitionTo(WorkOrderStatus.IN_PROGRESS, NOW.plusSeconds(60));
        var completed = inProgress.transitionTo(WorkOrderStatus.COMPLETED, NOW.plusSeconds(120));

        assertEquals(WorkOrderStatus.OPEN, open.status());
        assertEquals(WorkOrderStatus.IN_PROGRESS, inProgress.status());
        assertEquals(WorkOrderStatus.COMPLETED, completed.status());
    }

    @Test
    void rejectsSkippingDirectlyToCompleted() {
        var open = WorkOrder.create(
                "acme", "Investigate alert", null, WorkOrderPriority.HIGH,
                null, null, NOW);

        assertThrows(InvalidStateTransitionException.class,
                () -> open.transitionTo(WorkOrderStatus.COMPLETED, NOW.plusSeconds(60)));
    }

    @Test
    void rejectsPastDueDates() {
        assertThrows(IllegalArgumentException.class, () -> WorkOrder.create(
                "acme", "Investigate alert", null, WorkOrderPriority.HIGH,
                null, NOW.minusSeconds(1), NOW));
    }
}
