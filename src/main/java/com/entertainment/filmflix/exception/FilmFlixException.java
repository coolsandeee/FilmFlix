package com.entertainment.filmflix.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class FilmFlixException extends RuntimeException {
    public FilmFlixException() {
    }

    public FilmFlixException(String message) {
        super(message);
    }
}
