# ADR 0002: Start as a modular monolith

Status: accepted

## Context

Splitting a small work-order domain into networked services would add deployment,
consistency and observability costs without an independent scaling requirement.

## Decision

Use a hexagonal modular monolith. The domain has no Spring dependency;
application ports separate use cases from JPA and JWT adapters.

## Consequences

- Local development and transactions remain simple.
- Module boundaries are explicit and testable.
- A module can be extracted later when operational evidence justifies it.
