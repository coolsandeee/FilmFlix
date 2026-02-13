package com.entertainment.filmflix.exception;

import com.entertainment.filmflix.dto.response.ErrorResponseDTO;
import com.entertainment.filmflix.dto.response.GenericResponse;
import com.entertainment.filmflix.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<ErrorResponseDTO>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessages(errors);

        GenericResponse<ErrorResponseDTO> response = new GenericResponse<>();
        response.setMessage("Validation errors exist.");
        response.setResponse(errorResponseDTO);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(FilmFlixException.class)
    public ResponseEntity<Response<ErrorResponseDTO>> handleFilmFlixException(FilmFlixException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessages(errors);

        GenericResponse<ErrorResponseDTO> response = new GenericResponse<>();
        response.setMessage("Internal exception occurred.");
        response.setResponse(errorResponseDTO);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(NoDataException.class)
    public ResponseEntity<Response<ErrorResponseDTO>> handleNoDataException(NoDataException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessages(errors);

        GenericResponse<ErrorResponseDTO> response = new GenericResponse<>();
        response.setMessage("No data exists.");
        response.setResponse(errorResponseDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
