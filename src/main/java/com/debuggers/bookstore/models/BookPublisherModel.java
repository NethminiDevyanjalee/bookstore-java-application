package com.debuggers.bookstore.models;

import com.debuggers.bookstore.models.SqlDataModel;
import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookPublisherModel implements SqlDataModel {




    private int id;
    private String Name;
    private String email;
    private String phone;

    private int isDelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
            this.Name = resultSet.getString("name");
            this.email = resultSet.getString("email");
            this.phone = resultSet.getString("phone_no");
            this.isDelete = resultSet.getInt("is_delete");
        } catch (SQLException e) {
            throw new DataRepositoryException(e, e.getMessage());
        }
    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        columnValueMap.set("id", this.id);
        columnValueMap.set("name", this.Name);
        columnValueMap.set("email", this.email);
        columnValueMap.set("phone_no", this.phone);
        columnValueMap.set("is_delete", this.isDelete);
        return columnValueMap;
    }
}
