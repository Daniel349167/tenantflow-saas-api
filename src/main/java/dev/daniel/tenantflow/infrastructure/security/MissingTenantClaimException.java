package dev.daniel.tenantflow.infrastructure.security;

public class MissingTenantClaimException extends RuntimeException {

    public MissingTenantClaimException() {
        super("Authenticated token does not contain a tenant_id claim");
    }
}
