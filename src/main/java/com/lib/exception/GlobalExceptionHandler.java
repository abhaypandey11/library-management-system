package com.lib.exception;



import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lib.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Handles @Valid annotation failures
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "Validation failed", errors));
    }

    // ✅ Handles book/member not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // ✅ Handles book not available to issue
    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ApiResponse<?>> handleNotAvailable(
            BookNotAvailableException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // ✅ Handles duplicate ISBN or email
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicate(
            DuplicateResourceException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // ✅ Handles any other unexpected error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Something went wrong: " + ex.getMessage(), null));
    }



/*

## 💡 Key Things to Understand

**Why `extends RuntimeException`?**
```
RuntimeException = unchecked exception
→ You don't need try/catch everywhere
→ Just throw it anywhere and GlobalExceptionHandler catches it
```

**`@RestControllerAdvice` — how it works:**
```
Any layer throws exception
          ↓
GlobalExceptionHandler catches it
          ↓
Returns clean ApiResponse to user
          ↓
No ugly error stack trace exposed!

*/

}
