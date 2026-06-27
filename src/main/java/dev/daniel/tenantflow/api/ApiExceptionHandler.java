package dev.daniel.tenantflow.api;

import dev.daniel.tenantflow.application.WorkOrderNotFoundException;
import dev.daniel.tenantflow.domain.InvalidStateTransitionException;
import dev.daniel.tenantflow.infrastructure.security.MissingTenantClaimException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(WorkOrderNotFoundException.class)
    ProblemDetail notFound(WorkOrderNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "Work order not found", exception.getMessage());
    }

    @ExceptionHandler({InvalidStateTransitionException.class, OptimisticLockingFailureException.class})
    ProblemDetail conflict(RuntimeException exception) {
        return problem(HttpStatus.CONFLICT, "Work order conflict", exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail badRequest(IllegalArgumentException exception) {
        return problem(HttpStatus.BAD_REQUEST, "Invalid request", exception.getMessage());
    }

    @ExceptionHandler(MissingTenantClaimException.class)
    ProblemDetail missingTenant(MissingTenantClaimException exception) {
        return problem(HttpStatus.UNAUTHORIZED, "Invalid tenant context", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException exception) {
        var detail = problem(HttpStatus.BAD_REQUEST, "Validation failed", "One or more fields are invalid");
        var errors = new LinkedHashMap<String, String>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        detail.setProperty("errors", errors);
        return detail;
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        var problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        return problem;
    }
}
