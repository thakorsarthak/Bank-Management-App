/*
 * package com.example.bankapp.Exception;
 *
 * import org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.ExceptionHandler; import
 * org.springframework.web.bind.annotation.RestControllerAdvice;
 *
 * @RestControllerAdvice public class GlobalExceptionHandler {
 *
 * @ExceptionHandler(RuntimeException.class) public ResponseEntity<String>
 * handleRuntimeException(RuntimeException ex) { return
 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); } }
 *
 */
package com.example.bankapp.Exception;
// Must be top-level class, not inner class

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handles validation errors (like @NotBlank, @Size)
	@ExceptionHandler(CustomValidationException.class)
	public ResponseEntity<ApiError> handleCustomValidation(CustomValidationException ex) {

		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getErrors());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGlobal(Exception ex) {

		ApiError error = new ApiError(500, "Internal server error", null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

}
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//            errors.put(error.getField(), error.getDefaultMessage())
//        );
//
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//}