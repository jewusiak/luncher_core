package pl.luncher.v3.luncher_core.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserExtractionFromContextFailed extends ResponseStatusException {
    public UserExtractionFromContextFailed() {
        super(HttpStatus.NOT_ACCEPTABLE, "User can't be extracted from context!");
    }
}
