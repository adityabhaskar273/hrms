package com.example.hrms.hrms.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiError> responseStatus(ResponseStatusException e, HttpServletRequest r) {
    HttpStatusCode s = e.getStatusCode();
    return response(s, e.getReason() == null ? "Request failed" : e.getReason(), r, Map.of());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> validation(
      MethodArgumentNotValidException e, HttpServletRequest r) {
    Map<String, String> fields = new TreeMap<>();
    e.getBindingResult()
        .getFieldErrors()
        .forEach(x -> fields.putIfAbsent(x.getField(), x.getDefaultMessage()));
    return response(HttpStatus.BAD_REQUEST, "Request validation failed", r, fields);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> unreadable(
      HttpMessageNotReadableException e, HttpServletRequest r) {
    return response(HttpStatus.BAD_REQUEST, "Request body is invalid or missing", r, Map.of());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> constraint(ConstraintViolationException e, HttpServletRequest r) {
    return response(HttpStatus.BAD_REQUEST, "Request validation failed", r, Map.of());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> integrity(
      DataIntegrityViolationException e, HttpServletRequest r) {
    return response(
        HttpStatus.CONFLICT, "The request conflicts with an existing record", r, Map.of());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> unexpected(Exception e, HttpServletRequest r) {
    log.error("Unhandled API error for {}", r.getRequestURI(), e);
    return response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", r, Map.of());
  }

  private ResponseEntity<ApiError> response(
      HttpStatusCode status, String message, HttpServletRequest req, Map<String, String> fields) {
    String title = status instanceof HttpStatus h ? h.getReasonPhrase() : "Error";
    return ResponseEntity.status(status)
        .body(
            new ApiError(
                Instant.now(), status.value(), title, message, req.getRequestURI(), fields));
  }
}
