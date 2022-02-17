package com.debuggers.bookstore.views;

import com.debuggers.bookstore.repository.DataRepository;

import javax.swing.*;

public class User extends PageView{
    final private DataRepository dataRepository;

    private JPanel mainPanel;
    private JScrollPane jScrollPane;
    private JPanel formPanel;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtTelephoneNumber;
    private JButton btnSave;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JComboBox comboBox1;

    public User(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
    }
}