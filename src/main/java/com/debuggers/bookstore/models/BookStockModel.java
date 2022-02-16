package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;

public class BookStockModel implements SqlDataModel{


    @Override
    public void readSQL(ResultSet resultSet) throws DataRepositoryException {

    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        return null;
    }
}
