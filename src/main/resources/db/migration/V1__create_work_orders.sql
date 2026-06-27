CREATE TABLE work_orders (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(80) NOT NULL,
    title VARCHAR(160) NOT NULL,
    description TEXT,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    assignee_email VARCHAR(254),
    due_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_work_orders_tenant_created
    ON work_orders (tenant_id, created_at DESC);

CREATE INDEX idx_work_orders_tenant_status
    ON work_orders (tenant_id, status);
