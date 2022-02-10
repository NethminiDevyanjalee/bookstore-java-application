package com.debuggers.bookstore;

import com.debuggers.bookstore.input.Input;
import com.debuggers.bookstore.input.InputData;
import com.debuggers.bookstore.input.InputException;
import com.debuggers.bookstore.input.json.JsonFileInputData;
import com.debuggers.bookstore.input.json.JsonFileInputs;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;
import com.debuggers.bookstore.repository.sql.SqlDataRepository;
import com.debuggers.bookstore.views.Alert;
import com.debuggers.bookstore.views.Dashboard;

public class BookStoreApplication {

    public static void main(String[] args) {

        final Input input = new JsonFileInputs();

        try {

            final InputData inputData = input.getInputs();

            DataRepository dataRepository = new SqlDataRepository((JsonFileInputData) inputData);
            dataRepository.connect();

            new Dashboard(dataRepository);

        } catch (DataRepositoryException | InputException e) {

            Alert.showError("Error Occur:",e.getMessage());

        }

    }


}
