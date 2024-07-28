package pl.luncher.v3.luncher_core.common.exceptions;

import pl.luncher.v3.luncher_core.common.controllers.errorhandling.model.ErrorResponse;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(String message) {
        super(message);
    }
    
    public ErrorResponse toResponse() {
        return ErrorResponse.builder().message(getMessage()).build();
    }
}
