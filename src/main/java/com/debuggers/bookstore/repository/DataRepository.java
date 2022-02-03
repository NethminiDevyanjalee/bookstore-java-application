package com.debuggers.bookstore.repository;

import com.debuggers.bookstore.repository.sql.DataRepositoryException;

public interface DataRepository {
    void connect() throws DataRepositoryException;

    void executeQuery(String query) throws DataRepositoryException;
}
