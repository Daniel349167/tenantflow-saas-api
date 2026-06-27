package dev.daniel.tenantflow.infrastructure.security;

import dev.daniel.tenantflow.application.port.TenantContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtTenantContext implements TenantContext {

    @Override
    public String requiredTenantId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new MissingTenantClaimException();
        }
        String tenantId = jwt.getClaimAsString("tenant_id");
        if (tenantId == null || tenantId.isBlank()) {
            throw new MissingTenantClaimException();
        }
        return tenantId;
    }
}
