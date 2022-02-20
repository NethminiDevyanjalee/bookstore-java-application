package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookModel implements SqlDataModel {

    private int id;
    private String name;
    private String description;
    private String author;
    private String language;
    private String publisher;
    private String imageURL;
    private int authorId;
    private int categoryId;
    private int subCategoryId;
    private double price;
    private String isbn;
    private int publisherId;
    private int languageId;

    private int isDelete;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public void readSQL(ResultSet resultSet) throws DataRepositoryException {
        try {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("name");
            this.author = resultSet.getString("author");
            this.authorId = resultSet.getInt("author_id");
            this.publisherId = resultSet.getInt("publisher_id");
            this.categoryId = resultSet.getInt("category_id");
            this.subCategoryId = resultSet.getInt("sub_category_id");
            this.languageId = resultSet.getInt("language_id");
            this.language = resultSet.getString("language");
            this.price = resultSet.getDouble("price");
            this.isbn = resultSet.getString("isbn");
            this.publisher = resultSet.getString("publisher");
            this.description = resultSet.getString("description");
            this.imageURL = resultSet.getString("img_url");
            this.isDelete = 0;
        } catch (SQLException e) {
            throw new DataRepositoryException(e, e.getMessage());
        }
    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        columnValueMap.set("name", this.name);
        columnValueMap.set("description", this.description);
        columnValueMap.set("author_id", this.authorId);
        columnValueMap.set("publisher_id", this.publisherId);
        columnValueMap.set("language_id", this.languageId);
        columnValueMap.set("category_id", this.categoryId);
        columnValueMap.set("sub_category_id", this.subCategoryId);
        columnValueMap.set("isbn", this.isbn);
        columnValueMap.set("slug", this.isbn);
        columnValueMap.set("price", this.price);
        columnValueMap.set("is_delete", this.isDelete);
        if (this.imageURL != null) columnValueMap.set("img_url", this.imageURL);
        return columnValueMap;
    }
}
