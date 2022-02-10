package com.debuggers.bookstore.views;

import com.debuggers.bookstore.repository.DataRepository;

import javax.swing.*;

public class Home extends PageView{
    private JPanel mainPanel;

    private DataRepository dataRepository;

    public Home(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
    }
}
