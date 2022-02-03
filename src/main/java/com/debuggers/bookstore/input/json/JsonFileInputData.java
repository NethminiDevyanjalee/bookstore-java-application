package com.debuggers.bookstore.input.json;

import com.debuggers.bookstore.input.InputData;

public class JsonFileInputData implements InputData {
private String databaseHost;
private String databaseName;
private String databaseUserName;
private String getDatabasePassword;


    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUserName() {
        return databaseUserName;
    }

    public void setDatabaseUserName(String databaseUserName) {
        this.databaseUserName = databaseUserName;
    }

    public String getGetDatabasePassword() {
        return getDatabasePassword;
    }

    public void setGetDatabasePassword(String getDatabasePassword) {
        this.getDatabasePassword = getDatabasePassword;
    }

}
