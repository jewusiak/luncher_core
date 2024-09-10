package pl.luncher.v3.luncher_core.common.controllers.errorhandling;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.luncher.v3.luncher_core.common.controllers.errorhandling.model.ErrorResponse;
import pl.luncher.v3.luncher_core.common.domain.users.ForgottenPasswordIntentInvalidException;
import pl.luncher.v3.luncher_core.common.exceptions.DuplicateEntityException;
import pl.luncher.v3.luncher_core.common.permissions.MissingPermissionException;

@RestControllerAdvice(basePackages = "pl.luncher.v3.luncher_core.common.controllers")
public class CommonControllerAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler({DuplicateEntityException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  protected ErrorResponse handleDuplicateEntityException(DuplicateEntityException ex) {
    return ex.toResponse();
  }
  
  @ExceptionHandler({NoSuchElementException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected ErrorResponse handleNotFound(Exception ex) {
    return ErrorResponse.builder().message("Not found!").messageLocale("en_US").build();
  }

  @ExceptionHandler({ForgottenPasswordIntentInvalidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ErrorResponse handleUnknownBadRequest(Exception ex) {
    return ErrorResponse.builder().message("Bad request").messageLocale("en_US").build();
  }

  @ExceptionHandler({MissingPermissionException.class, AccessDeniedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  protected ErrorResponse handleForbidden(Exception ex) {
    return ErrorResponse.builder().message("Forbidden").cause(ex.getMessage()).messageLocale("en_US").build();
  }

//  Other exceptions
//  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//  @ExceptionHandler(Exception.class)
//  protected ErrorResponse handleOtherExceptions(Exception exception) {
//    return ErrorResponse.builder()
//        .message(Optional.ofNullable(exception).map(Exception::getMessage).orElse(null)).cause(
//            Optional.ofNullable(exception).map(Throwable::getCause).map(Throwable::getMessage)
//                .orElse(null)).build();
//  }
}
