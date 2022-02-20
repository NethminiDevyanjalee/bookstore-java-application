package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookStockModel implements SqlDataModel {
    private int id;
    private String name;
    private String isbn;
    private int quantity;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void readSQL(ResultSet resultSet) throws DataRepositoryException {
        try {
            this.id = resultSet.getInt("id");
            this.quantity = resultSet.getInt("qty");
            this.name = resultSet.getString("name");
            this.isbn = resultSet.getString("isbn");
        } catch (SQLException e) {
            new DataRepositoryException(e, e.getMessage());
        }
    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        return null;
    }


}
