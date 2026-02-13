package com.entertainment.filmflix.exception;

public class NoDataException extends RuntimeException {
    public NoDataException() {
    }

    public NoDataException(String message) {
        super(message);
    }
}
