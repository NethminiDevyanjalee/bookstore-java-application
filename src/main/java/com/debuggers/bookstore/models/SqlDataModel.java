package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;


public interface SqlDataModel {

    void readSQL(ResultSet resultSet) throws DataRepositoryException;

    ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException;

}
