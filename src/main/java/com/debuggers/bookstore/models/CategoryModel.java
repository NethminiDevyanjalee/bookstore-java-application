package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryModel implements SqlDataModel {
    private int id;
    private String Category;
    private String subCategoriesStr;
    private List<String> subCategoriesList = new ArrayList<>();
    private List<Integer> subCategoriesIDSList = new ArrayList<>();
    private int isDelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubCategoriesStr() {
        return subCategoriesStr;
    }

    public void setSubCategoriesStr(String subCategoriesStr) {
        this.subCategoriesStr = subCategoriesStr;
    }

    public List<String> getSubCategoriesList() {
        return subCategoriesList;
    }

    public void setSubCategoriesList(List<String> subCategoriesList) {
        this.subCategoriesList = subCategoriesList;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public List<Integer> getSubCategoriesIDSList() {
        return subCategoriesIDSList;
    }

    @Override
    public void readSQL(ResultSet resultSet) throws DataRepositoryException {
        try {
            this.id = resultSet.getInt("id");
            this.subCategoriesStr = resultSet.getString("sub_category");
            this.Category = resultSet.getString("category");
            this.isDelete = resultSet.getInt("is_delete");

            if (!this.subCategoriesStr.equals("N/A")) {
                for (String s : this.subCategoriesStr.split(",")) {
                    this.subCategoriesList.add(s);
                }
                for (String s : resultSet.getString("sub_category_ids").split(",")) {
                    this.subCategoriesIDSList.add(Integer.parseInt(s));
                }
            }

        } catch (SQLException e) {
            throw new DataRepositoryException(e, e.getMessage());
        }
    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        columnValueMap.set("category", this.Category);
        columnValueMap.set("is_delete", this.isDelete);
        return columnValueMap;
    }
}
