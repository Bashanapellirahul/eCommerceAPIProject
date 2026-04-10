package com.ECommerceAPI.ECommerceAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─── 404 Not Found ────────────────────────────────────────────────
    // Catches: ResourceNotFoundException thrown anywhere in the app
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                "Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // ─── 403 Forbidden ────────────────────────────────────────────────
    // Catches: UnauthorizedException — user tried to access something not theirs
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                "Forbidden", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    // ─── 400 Bad Request — Business logic ─────────────────────────────
    // Catches: BadRequestException — invalid business-level data
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ─── 400 Bad Request — Validation failures ─────────────────────────
    // Catches: When @Valid fails in a Controller (e.g. @NotBlank violated)
    // This is a DIFFERENT exception type — Spring throws this one automatically
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(err->
                {
                    String fieldName = ((FieldError) err).getField();
                    String message = err.getDefaultMessage();
                    errors.put(fieldName,message);
                });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // ─── 500 Internal Server Error ─────────────────────────────────────
    // Catches: ANYTHING else that wasn't caught above
    // This is your safety net — should rarely trigger in a well-written app
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),  // 500
                "Internal Server Error",
                // NEVER expose ex.getMessage() here in production —
                // it might leak internal details. Use a generic message.
                "Something went wrong. Please try again later."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRunTime(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"Bad request",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
