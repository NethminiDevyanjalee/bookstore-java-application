package com.debuggers.bookstore.input.json;

import com.debuggers.bookstore.input.Input;
import com.debuggers.bookstore.input.InputException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonFileInputs implements Input {

    @Override
    public JsonFileInputData getInputs() throws InputException {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("config.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject json = (JSONObject) obj;

            JsonFileInputData jsonFileInputData = new JsonFileInputData();

            // database config
            JSONObject db = (JSONObject) json.get("database");
            jsonFileInputData.setDatabaseHost((String) db.get("host"));
            jsonFileInputData.setDatabaseName((String) db.get("name"));
            jsonFileInputData.setDatabaseUserName((String) db.get("user"));
            jsonFileInputData.setGetDatabasePassword((String) db.get("password"));


            return jsonFileInputData;

        } catch (FileNotFoundException e) {
            throw new InputException("config file not found");
        } catch (IOException e) {
            throw new InputException("config file not found");
        } catch (ParseException e) {
            throw new InputException("config format error");
        }

    }
}
