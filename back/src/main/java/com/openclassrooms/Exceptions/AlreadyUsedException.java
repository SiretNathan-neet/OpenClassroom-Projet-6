package com.openclassrooms.Exceptions;

/**
 * Exception levée lorsque qu'un utilisateur tente d'utiliser un email ou un nom d'utilisateur déjà utilisé.
 */

public class AlreadyUsedException extends RuntimeException {
    public AlreadyUsedException(String message) {
        super(message);
    }
}
