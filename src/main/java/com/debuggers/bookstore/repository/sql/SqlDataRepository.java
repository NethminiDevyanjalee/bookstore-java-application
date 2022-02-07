package com.debuggers.bookstore.repository.sql;

import com.debuggers.bookstore.input.json.JsonFileInputData;
import com.debuggers.bookstore.models.SqlDataModel;
import com.debuggers.bookstore.repository.ColumnValueMap;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlDataRepository implements DataRepository {

    private final JsonFileInputData jsonFileInputData;
    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private String selectQuery = "SELECT * FROM $TABLE";
    private String insertQuery = "INSERT INTO $TABLE($COLUMNS) VALUES($VALUES)";

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
            throw new DataRepositoryException(e, e.getMessage());
        }
    }

    @Override
    public ResultSet executeQuery(String query) throws DataRepositoryException {

        try {

            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {

            throw new DataRepositoryException(e, e.getMessage());

        }

        return resultSet;

    }


    @Override
    public List<SqlDataModel> fetch(String table, Class dataClass) throws DataRepositoryException {

        List<SqlDataModel> dataList = new ArrayList<>();

        try {

            selectQuery = selectQuery.replace("$TABLE", table);
            preparedStatement = conn.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SqlDataModel dataModel = (SqlDataModel) dataClass.getDeclaredConstructor().newInstance();
                dataModel.readSQL(resultSet);
                dataList.add(dataModel);
            }

        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            throw new DataRepositoryException(e, e.getMessage());

        }

        return dataList;
    }

    @Override
    public void insert(String table, SqlDataModel sqlDataModel) throws DataRepositoryException {
        ColumnValueMap columnValueMap = sqlDataModel.writeSQL(new ColumnValueMap());
        insertQuery = insertQuery.replace("$TABLE", table);
        insertQuery = insertQuery.replace("$COLUMNS", columnValueMap.getStrColumns());
        insertQuery = insertQuery.replace("$VALUES", columnValueMap.getStrValues());

        try {
            preparedStatement = conn.prepareStatement(insertQuery);

            int i = 1;
            for (Object obj : columnValueMap.getValues()) {
                preparedStatement.setObject(i, obj);
                i++;
            }

            preparedStatement.execute();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(String table, SqlDataModel sqlDataModel) throws DataRepositoryException {
        // TODO:Implementation
    }

    @Override
    public void delete(String table, SqlDataModel sqlDataModel) throws DataRepositoryException {
        // TODO:Implementation
    }

}
