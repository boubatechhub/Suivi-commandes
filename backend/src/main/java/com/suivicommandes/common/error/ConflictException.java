package com.suivicommandes.common.error;

/**
 * La requête est valide mais contredit l'état actuel des données (doublon, règle métier) :
 * traduite en HTTP 409 par {@link GlobalExceptionHandler}.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
