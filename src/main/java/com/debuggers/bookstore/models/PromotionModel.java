package com.debuggers.bookstore.models;

import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromotionModel implements SqlDataModel{
    private int ID;
    private String CouponCode;
    private float Discount;
    private Date From;
    private Date To;
    private int isDelete;

    public int getId() {
        return ID;
    }

    public String getCouponCode(){
        return CouponCode;
    }

    public float getDiscount() {
        return Discount;
    }

    public Date getFrom() {
        return From;
    }

    public Date getTo() {
        return To;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public void setCouponCode(String couponCode) {
        this.CouponCode = couponCode;
    }

    public void setDiscount(float discount) {
        this.Discount = discount;
    }

    public void setFrom(Date from) {
        this.From = from;
    }

    public void setTo(Date to) {
        this.To = to;
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
            this.ID = resultSet.getInt("id");
            this.CouponCode = resultSet.getString("code");
            this.Discount = resultSet.getFloat("discount");
            this.From = Date.valueOf(resultSet.getString("from"));
            this.To = Date.valueOf(resultSet.getString("to"));
            this.isDelete = resultSet.getInt("is_delete");
        } catch (SQLException e) {
            throw new DataRepositoryException(e, e.getMessage());
        }
    }

    @Override
    public ColumnValueMap writeSQL(ColumnValueMap columnValueMap) throws DataRepositoryException {
        columnValueMap.set("id", this.ID);
        columnValueMap.set("code", this.CouponCode);
        columnValueMap.set("discount", this.Discount);
        columnValueMap.set("from", this.From);
        columnValueMap.set("to", this.To);
        columnValueMap.set("is_delete", this.isDelete);
        return columnValueMap;
    }




}
