package com.openclassrooms.Exceptions;

/**
 * Exception levée lorsqu'une ressource demandée est introuvable.
 * Retourne un HTTP 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}