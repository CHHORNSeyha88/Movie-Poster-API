package com.movieflex.MovieFlex_Api.exception;

public class FileExistsException extends RuntimeException {
    public FileExistsException(String message) {
        super(message);

    }
}
