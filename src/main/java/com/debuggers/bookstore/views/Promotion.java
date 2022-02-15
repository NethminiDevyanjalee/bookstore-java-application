package com.debuggers.bookstore.views;

import com.debuggers.bookstore.repository.DataRepository;

import javax.swing.*;

public class Promotion extends PageView{
    final private DataRepository dataRepository;

    private JPanel mainPanel;

    public Promotion(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
    }
}
