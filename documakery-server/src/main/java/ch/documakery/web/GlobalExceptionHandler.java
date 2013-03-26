package ch.documakery.web;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ch.documakery.validation.dto.ValidationErrorMessageDto;

/**
 * Global handler for exceptions. Enhances exception with information in the body.
 * 
 * @author Marco Jakob
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
    
    // Create dto for the body of the exception message.
    ValidationErrorMessageDto validationErrors = new ValidationErrorMessageDto();
    
    // Add field errors
    for (FieldError fieldError : fieldErrors) {
      validationErrors.addError(fieldError.getField(), fieldError.getDefaultMessage());
    }
    
    // Add object errors
    for (ObjectError objectError : globalErrors) {
      validationErrors.addError(objectError.getObjectName(), objectError.getDefaultMessage());
    }
    
    return handleExceptionInternal(ex, validationErrors, headers, status, request);
  }
}
