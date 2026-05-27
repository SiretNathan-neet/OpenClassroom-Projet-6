package com.openclassrooms.Exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestionnaire d'exceptions global pour l'application.
 * Intercepte les exceptions de type AlreadyUsedException et RuntimeException,
 * retourne une réponse HTTP appropriée avec un message d'erreur.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyUsedException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyUsedException(AlreadyUsedException ex) {
        return ResponseEntity
            /** Erreur 409 plus adapté que 400 pour ce projet */
            .status(HttpStatus.CONFLICT) 
            .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", e.getMessage()));
    }
}
