package com.util;

import java.util.NoSuchElementException;

public class ResultError<T> {
    private final T val;
    private final Result result;
    private final String errorMessage;
    
    private ResultError(T val, Result result, String err) {
        this.val = val;
        this.result = result;
        this.errorMessage = err;
    }

    public T get() {
        if (this.val == null) {
            throw new NoSuchElementException("no value present");
        } else {
            return this.val;
        }
    }

    public boolean isPresent() {
        return this.result == Result.FULL;
    }

    public String getError() {
        return this.errorMessage;
    }

    public static <T> ResultError<T> empty(String err) {
        return new ResultError<>(null, Result.EMPTY, err);
    }

    public static <T> ResultError<T> full(T obj) {
        if (obj == null) {
            return new ResultError<>(null, Result.EMPTY, "null obj passed into optional");
        }
        return new ResultError<>(obj, Result.FULL, "");
    }

    enum Result {
        EMPTY(),
        FULL();
    }
}
