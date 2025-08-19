package com.project.movies.exception;

public class OperationNotPermittedException extends RuntimeException  {
    public OperationNotPermittedException(String msg) {
        super(msg);
    }
}
