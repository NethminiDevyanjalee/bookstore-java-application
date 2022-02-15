package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.views.PageView;

import javax.swing.*;

public class Book extends PageView {
    private DataRepository dataRepository;
    private JPanel mainPanel;

    public Book(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
    }
}
