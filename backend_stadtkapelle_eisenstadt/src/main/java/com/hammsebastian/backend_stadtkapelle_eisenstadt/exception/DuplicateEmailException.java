package com.hammsebastian.backend_stadtkapelle_eisenstadt.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
