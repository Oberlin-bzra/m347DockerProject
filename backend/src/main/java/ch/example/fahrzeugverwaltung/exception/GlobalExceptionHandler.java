package ch.example.fahrzeugverwaltung.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fängt Validierungsfehler ab und gibt sie in einem übersichtlichen
 * JSON-Format zurück, statt der langen Standard-Fehlermeldung von Spring.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fehler = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                fehler.put(fe.getField(), fe.getDefaultMessage()));
        return ResponseEntity.badRequest().body(fehler);
    }
}
