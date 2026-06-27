# ADR 0001: Tenant identity comes only from the verified token

Status: accepted

## Context

Accepting a tenant ID from a request header or body allows a caller to probe a
different tenant unless every code path revalidates that value.

## Decision

The application reads `tenant_id` exclusively from the verified JWT. Repository
ports require a tenant ID for every lookup, and the API never accepts it as user
input.

## Consequences

- Cross-tenant access is denied by construction in application queries.
- Background jobs need an explicit system tenant context.
- Defense in depth with PostgreSQL row-level security remains a future option.
