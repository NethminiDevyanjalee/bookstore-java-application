package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.views.PageView;

import javax.swing.*;

public class Stock extends PageView {
    final private DataRepository dataRepository;
    private JPanel mainPanel;

    public Stock(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
    }
}
