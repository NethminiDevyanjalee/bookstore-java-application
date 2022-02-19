package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel implements SqlDataModel {


    private String firstName;
    private String lastName;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void readSQL(ResultSet resultSet) throws DataRepositoryException {
        try {
            this.firstName = resultSet.getString("fname");
            this.lastName = resultSet.getString("lname");
            this.email = resultSet.getString("email");

        } catch (SQLException e) {

            throw new DataRepositoryException(e,e.getMessage());

        }
    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        return null;
    }
}
