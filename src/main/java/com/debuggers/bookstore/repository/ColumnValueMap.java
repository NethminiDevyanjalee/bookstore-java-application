package com.debuggers.bookstore.repository;

import java.util.ArrayList;
import java.util.List;

public class ColumnValueMap {
    private String strColumns;
    private String strValues;
    private String strUpdates;
    private final List<Object> values = new ArrayList<>();


    public void set(String column, Object value) {

        if (strColumns == null) {
            strColumns = column;
            strValues = "?";
            strUpdates = String.format("SET %s = ?",column);
        } else {
            strColumns += "," + column;
            strValues += ",?";
            strUpdates += String.format(",%s = ?",column);
        }

        values.add(value);

    }


    public String getStrValues() {
        return strValues;
    }

    public String getStrColumns() {
        return strColumns;
    }

    public String getStrUpdates() {
        return strUpdates;
    }

    public List<Object> getValues() {
        return values;
    }
}
