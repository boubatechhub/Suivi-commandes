package com.suivicommandes.common.error;

/** Ressource demandée introuvable : traduite en HTTP 404 par {@link GlobalExceptionHandler}. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
