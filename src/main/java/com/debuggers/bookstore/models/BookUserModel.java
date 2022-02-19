package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookUserModel implements SqlDataModel{

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int userRoleId;

    private int isDelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public void readSQL(ResultSet resultSet) throws DataRepositoryException {
        try {
            this.id = resultSet.getInt("id");
            this.firstName = resultSet.getString("fname");
            this.lastName = resultSet.getString("lname");
            this.email = resultSet.getString("email");
            this.isDelete = resultSet.getInt("is_delete");
        } catch (SQLException e) {
            throw new DataRepositoryException(e, e.getMessage());
        }

    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        columnValueMap.set("id", this.id);
        columnValueMap.set("fname", this.firstName);
        columnValueMap.set("lname", this.lastName);
        columnValueMap.set("email", this.email);
        columnValueMap.set("is_delete", this.isDelete);
        return columnValueMap;
    }
}
