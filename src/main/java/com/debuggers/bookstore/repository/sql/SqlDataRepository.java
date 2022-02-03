package com.debuggers.bookstore.repository.sql;

import com.debuggers.bookstore.input.json.JsonFileInputData;
import com.debuggers.bookstore.repository.DataRepository;

import java.sql.*;

public class SqlDataRepository implements DataRepository {

    final private JsonFileInputData jsonFileInputData;
    private Connection conn = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public SqlDataRepository(JsonFileInputData jsonFileInputData) {
        this.jsonFileInputData = jsonFileInputData;
    }


    @Override
    public void connect() throws DataRepositoryException {
        try {
            String url = "jdbc:mysql://" + jsonFileInputData.getDatabaseHost() +
                    "/" + jsonFileInputData.getDatabaseName() + "?user="
                    + jsonFileInputData.getDatabaseUserName() + "&password=" + jsonFileInputData.getGetDatabasePassword();
            conn =
                    DriverManager.getConnection(url);

        } catch (SQLException e) {
            throw new DataRepositoryException(e, "Database connection failed, Check database name,password and host");
        }
    }

    @Override
    public void executeQuery(String query) throws DataRepositoryException {

        try {

            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {

            throw new DataRepositoryException(e, "Database error.");

        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new DataRepositoryException(e, e.getMessage());
                }

                resultSet = null;
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new DataRepositoryException(e, e.getMessage());
                }

                statement = null;
            }
        }

    }
}
