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

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.bankapp.DTO.GlobalAPIResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handles validation errors (like @NotBlank, @Size)
	@ExceptionHandler(CustomValidationException.class)
	public ResponseEntity<GlobalAPIResponseDTO<List<FieldError>>> handleCustomValidation(CustomValidationException ex) {

//		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getErrors());
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);


	    if (ex.getErrors() != null) {
	        return ResponseEntity.badRequest()
	                .body(new GlobalAPIResponseDTO<>(ex.getMessage(), false, ex.getErrors()));
	    }

	    return ResponseEntity.badRequest()
	            .body(new GlobalAPIResponseDTO<>(ex.getMessage(), false));

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGlobal(Exception ex) {

		ApiError error = new ApiError(500, "Internal server error", null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}



//	@ExceptionHandler(CustomValidationException.class)
//    public ResponseEntity<GlobalAPIResponseDTO<?>> handleValidation(CustomValidationException ex) {
//
//        return ResponseEntity.badRequest()
//                .body(new GlobalAPIResponseDTO<>(ex.getMessage(), false));
//    }

//	@ExceptionHandler(InvalidStatusChange.class)
//	public ResponseEntity<GlobalAPIResponseDTO<?>> handleInvalidStatus (InvalidStatusChange ex){
//		return ResponseEntity.badRequest()
//                .body(new GlobalAPIResponseDTO<>(ex.getMessage(), false));
//
//	}
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