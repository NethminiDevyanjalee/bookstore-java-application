package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryModel implements SqlDataModel{
    private int CategoryId;
    private int SubCategoryId;
    private String Category;
    private String SubCategory;
    private int IsDelete;

    public int getCategoryId() {
        return CategoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public String getCategory() {
        return Category;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public int getIsDelete() {
        return IsDelete;
    }

    public void setCategoryId(int categoryId) {
        this.CategoryId = categoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.SubCategoryId = subCategoryId;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public void setSubCategory(String subCategory) {
        this.SubCategory = subCategory;
    }

    public void setIsDelete(int isDelete) {
        this.IsDelete = isDelete;
    }

    @Override
    public void readSQL(ResultSet resultSet) throws DataRepositoryException {
        try {
            this.CategoryId = resultSet.getInt("category_id");
            this.SubCategoryId = resultSet.getInt("id");
            this.Category = resultSet.getString("category");
            this.SubCategory = resultSet.getString("sub_category");
            this.IsDelete = resultSet.getInt("is_delete");

        } catch (SQLException e) {
            throw new DataRepositoryException(e, e.getMessage());
        }
    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        return null;
    }
}
