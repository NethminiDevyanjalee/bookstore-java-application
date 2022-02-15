package com.debuggers.bookstore.views;

import com.debuggers.bookstore.repository.DataRepository;

import javax.swing.*;

public class User extends PageView{
    final private DataRepository dataRepository;

    private JPanel mainPanel;

    public User(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
    }
}