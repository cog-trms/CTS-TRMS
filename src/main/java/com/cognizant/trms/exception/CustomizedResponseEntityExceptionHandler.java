package com.cognizant.trms.exception;

import com.cognizant.trms.dto.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Aravindan Dandapani
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TRMSException.EntityNotFoundException.class)
    public final ResponseEntity handleNotFountExceptions(Exception ex, WebRequest request) {
        Response response = Response.notFound();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TRMSException.DuplicateEntityException.class)
    public final ResponseEntity handleNotFountExceptions1(Exception ex, WebRequest request) {
        Response response = Response.duplicateEntity();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TRMSException.AccessDeniedException.class)
    public final ResponseEntity handleNotFountExceptions2(Exception ex, WebRequest request) {
        Response response = Response.accessDenied();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);

    }

    // @Validate For Validating Path Variables and Request Parameters
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(Exception ex, WebRequest request)  {
        Response response = Response.badRequest();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    // @Validate For Validating Path Variables and Request Parameters
    @ExceptionHandler(TRMSException.BadRequestException.class)
    public ResponseEntity constraintViolationException1(Exception ex, WebRequest request)  {
        Response response = Response.badRequest();
        response.addErrorMsgToResponse(ex.getMessage(), ex);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }


    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

        //Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().collect(
        //        Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

    }

}
