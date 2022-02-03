package com.debuggers.bookstore.repository.sql;

public class DataRepositoryException extends Throwable {
    public DataRepositoryException(Exception e, String message) {
        super(message, e);
    }
}