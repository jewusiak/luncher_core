package pl.luncher.v3.luncher_core.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientRoleException extends ResponseStatusException {
    public InsufficientRoleException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
