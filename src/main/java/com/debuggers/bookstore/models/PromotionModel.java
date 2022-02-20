package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromotionModel implements SqlDataModel{
    private int id;
    private String CouponCode;
    private float discount;
    private Date From;
    private Date to;
    private int isDelete;

    public int getId() {
        return id;
    }

    public String getCouponCode(){
        return CouponCode;
    }

    public float getDiscount() {
        return discount;
    }

    public Date getFrom() {
        return From;
    }

    public Date getTo() {
        return to;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCouponCode(String couponCode) {
        this.CouponCode = couponCode;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setFrom(Date from) {
        this.From = from;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public int getIsDelete(){
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public void readSQL(ResultSet resultSet) throws DataRepositoryException {
        try {
            this.id = resultSet.getInt("id");
            this.CouponCode = resultSet.getString("code");
            this.discount = resultSet.getFloat("discount");
            this.From = Date.valueOf(resultSet.getString("from"));
            this.to = Date.valueOf(resultSet.getString("to"));
            this.isDelete = resultSet.getInt("is_delete");
        } catch (SQLException e) {
            throw new DataRepositoryException(e, e.getMessage());
        }
    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        columnValueMap.set("id", this.id);
        columnValueMap.set("`code`", this.CouponCode);
        columnValueMap.set("discount", this.discount);
        columnValueMap.set("`from`", this.From);
        columnValueMap.set("`to`", this.to);
        columnValueMap.set("is_delete", this.isDelete);
        return columnValueMap;
    }




}
