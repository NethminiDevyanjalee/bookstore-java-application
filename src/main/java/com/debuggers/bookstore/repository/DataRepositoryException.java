package com.debuggers.bookstore.repository;

public class DataRepositoryException extends Throwable {
    public DataRepositoryException(Exception e, String message) {
        super(message, e);
    }
}