package com.openclassrooms.Exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestionnaire d'exceptions global pour l'application.
 * Centralise la gestion des erreurs et retourne les réponses HTTP adaptées.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 409 — Email ou username déjà utilisé */
    @ExceptionHandler(AlreadyUsedException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyUsedException(AlreadyUsedException e) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of("message", e.getMessage()));
    }

    /** 404 — Ressource introuvable (article, thème, utilisateur) */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", e.getMessage()));
    }

    /** 400 — Données invalides (mot de passe, format email…) */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", e.getMessage()));
    }

    /** 401 — Identifiant ou mot de passe incorrect */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Identifiant ou mot de passe incorrect"));
    }

    /** 500 — Erreur inattendue */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "Une erreur inattendue est survenue"));
    }
}