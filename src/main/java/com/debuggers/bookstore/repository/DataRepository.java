package com.debuggers.bookstore.repository;

import com.debuggers.bookstore.models.SqlDataModel;

import java.sql.ResultSet;
import java.util.List;

public interface DataRepository {
    void connect() throws DataRepositoryException;

    ResultSet executeQuery(String query) throws DataRepositoryException;

    List<SqlDataModel> get( Class dataClass) throws DataRepositoryException;

    void table(String tableName);

    void  where(String columnName,Object value);

    void  where(String columnName,String operator,Object value);

    void  orWhere(String columnName,Object value);

    void  orWhere(String columnName,String operator,Object value);

    void insert(SqlDataModel sqlDataModel) throws DataRepositoryException;

    void update(SqlDataModel sqlDataModel) throws DataRepositoryException;

    void delete() throws DataRepositoryException;

}
