package com.debuggers.bookstore.repository;

import com.debuggers.bookstore.models.SqlDataModel;

import java.sql.ResultSet;
import java.util.List;

public interface DataRepository {
    void connect() throws DataRepositoryException;

    ResultSet executeQuery(String query) throws DataRepositoryException;

    List<SqlDataModel> fetch(String table, Class dataClass) throws DataRepositoryException;

    void insert(String table,SqlDataModel sqlDataModel) throws DataRepositoryException;

    void update(String table,SqlDataModel sqlDataModel) throws DataRepositoryException;

    void delete(String table,SqlDataModel sqlDataModel) throws DataRepositoryException;

}
