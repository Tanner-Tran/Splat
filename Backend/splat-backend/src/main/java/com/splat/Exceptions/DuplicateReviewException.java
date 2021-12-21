package com.splat.Exceptions;

public class DuplicateReviewException extends Exception {
    public DuplicateReviewException() {

    }

    public DuplicateReviewException(String message)
    {
        super(message);
    }
}
