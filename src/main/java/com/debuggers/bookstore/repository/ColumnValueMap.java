package com.debuggers.bookstore.repository;

import java.util.ArrayList;
import java.util.List;

public class ColumnValueMap {
    private String strColumns;
    private String strValues;
    private final List<Object> values = new ArrayList<>();


    public void set(String column, Object value) {

        if (strColumns == null) {
            strColumns = column;
            strValues = "?";
        } else {
            strColumns += "," + column;
            strValues += ",?";
        }

        values.add(value);

    }


    public String getStrValues() {
        return strValues;
    }

    public String getStrColumns() {
        return strColumns;
    }

    public List<Object> getValues() {
        return values;
    }
}
