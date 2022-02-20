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
    private Connection conn;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private String table;
    private String whereQuery;
    private List<Object> whereValues = new ArrayList<>();

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
    public void execute(String query) throws DataRepositoryException {

        System.out.println(query);
        try {

            statement = conn.createStatement();
            statement.execute(query);

        } catch (SQLException e) {

            throw new DataRepositoryException(e, e.getMessage());

        }
    }

    @Override
    public List<SqlDataModel> executeQuery(String query, Class dataClass) throws DataRepositoryException {
        List<SqlDataModel> dataList = new ArrayList<>();
        try {

            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
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
    public void createStatement() {
        whereValues.clear();
        whereQuery = null;
    }

    @Override
    public void createStatement(String tableName) {
        table = tableName;
        whereValues.clear();
        whereQuery = null;
    }

    @Override
    public void where(String columnName, Object value) {
        setWhere(columnName, "=", value, "AND");
    }

    @Override
    public void where(String columnName, String operator, Object value) {
        setWhere(columnName, operator, value, "AND");
    }

    @Override
    public void orWhere(String columnName, Object value) {
        setWhere(columnName, "=", value, "OR");
    }

    @Override
    public void orWhere(String columnName, String operator, Object value) {
        setWhere(columnName, operator, value, "OR");
    }

    private void setWhere(String columnName, String operator, Object value, String logicalOperator) {
        whereValues.add(value);

        if (whereQuery == null) {
            whereQuery = String.format("\tWHERE %s %s ?", columnName, operator);
        } else {
            whereQuery += String.format("\t%s %s %s ?", logicalOperator, columnName, operator);
        }

    }

    @Override
    public List<SqlDataModel> get(Class dataClass) throws DataRepositoryException {

        if (table == null) throw new DataRepositoryException("Table new can not be null");

        List<SqlDataModel> dataList = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", table);


        try {

            if (!whereValues.isEmpty()) {

                sql += whereQuery;
                preparedStatement = conn.prepareStatement(sql);

                int i = 1;

                for (Object obj : whereValues) {
                    preparedStatement.setObject(i, obj);
                    i++;
                }

            } else {

                preparedStatement = conn.prepareStatement(sql);

            }

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
    public Object insert(SqlDataModel sqlDataModel) throws DataRepositoryException {

        Object insert = 0;

        if (table == null) throw new DataRepositoryException("Table new can not be null");

        ColumnValueMap columnValueMap = sqlDataModel.writeSQL(new ColumnValueMap());

        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", table, columnValueMap.getStrColumns(), columnValueMap.getStrValues());

        try {

            preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            for (Object obj : columnValueMap.getValues()) {
                preparedStatement.setObject(i, obj);
                i++;
            }

            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                insert = rs.getObject(0);
            }


        } catch (SQLException e) {

            throw new DataRepositoryException(e, e.getMessage());

        }

        return insert;
    }

    @Override
    public void update(SqlDataModel sqlDataModel) throws DataRepositoryException {

        if (table == null) throw new DataRepositoryException("Table new can not be null");

        ColumnValueMap columnValueMap = sqlDataModel.writeSQL(new ColumnValueMap());

        String sql = String.format("UPDATE %s %s", table, columnValueMap.getStrUpdates());

        List<Object> values = columnValueMap.getValues();


        if (!whereValues.isEmpty()) {
            sql += whereQuery;
            values.addAll(whereValues);
        }

        try {

            preparedStatement = conn.prepareStatement(sql);

            int i = 1;
            for (Object obj : columnValueMap.getValues()) {
                preparedStatement.setObject(i, obj);
                i++;
            }

            preparedStatement.execute();

        } catch (SQLException e) {

            throw new DataRepositoryException(e, e.getMessage());

        }
    }

    @Override
    public void delete() throws DataRepositoryException {

        if (table == null) throw new DataRepositoryException("Table new can not be null");

        String sql = String.format("DELETE FROM %s", table);

        try {

            if (!whereValues.isEmpty()) {

                sql += whereQuery;
                preparedStatement = conn.prepareStatement(sql);

                int i = 1;

                for (Object obj : whereValues) {
                    preparedStatement.setObject(i, obj);
                    i++;
                }

            } else {

                preparedStatement = conn.prepareStatement(sql);

            }

            preparedStatement.execute();

        } catch (SQLException e) {

            throw new DataRepositoryException(e, e.getMessage());

        }
    }

}
