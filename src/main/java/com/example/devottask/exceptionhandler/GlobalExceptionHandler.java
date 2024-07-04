package com.example.devottask.exceptionhandler;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", Instant.now());

    String message = e.getMessage();
    HttpStatus status;

    switch (message) {
      case "Account not found":
      case "Not logged in":
        status = HttpStatus.UNAUTHORIZED;
        break;
      case "No expense found with given ID and account":
      case "No category found with given ID and account":
      case "Email is already in use":
      case "Account with given id not found":
      case "Invalid email format":
      case "Category with that name already exists":
      case "Insufficient balance.":
      case "Category not found":
      case "Cannot delete category as it has associated expenses.":
      case "Category with that name does not exist":
        status = HttpStatus.BAD_REQUEST;
        break;
      default:
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    body.put("errors", message);

    return new ResponseEntity<>(body, status);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", Instant.now());
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
        .collect(Collectors.joining(", "));

    body.put("errors", message);

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", Instant.now());
    body.put("message", ex.getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", Instant.now());
    body.put("message", "Invalid date format. Please use the format YYYY-MM-DD.");

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}
