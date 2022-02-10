package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.models.BookAuthorModel;
import com.debuggers.bookstore.models.SqlDataModel;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;
import com.debuggers.bookstore.views.PageView;
import com.debuggers.bookstore.views.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Author extends PageView {
    private JPanel mainPanel;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtTelephoneNumber;
    private JButton btnSave;
    private JTable table;
    private JScrollPane jScrollPane;
    private JLabel labelFirstName;
    private JLabel labelLastName;
    private JLabel labelEmail;
    private JLabel labelTelephoneNumber;
    private JButton btnEdit;
    private JButton btnDelete;
    private DataRepository dataRepository;

    public Author(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        dataRepository.table("book_author");
        add(mainPanel);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                String email = txtEmail.getText();
                String telephoneNumber = txtTelephoneNumber.getText();

                if(firstName.isEmpty()){
                    labelFirstName.setText("*First Name is Empty ");
                }
                if(lastName.isEmpty()){
                    labelLastName.setText("*Last Name is Empty ");
                }
                if(email.isEmpty()){
                    labelEmail.setText("*Email is Empty ");
                }
                if(telephoneNumber.isEmpty()){
                    labelTelephoneNumber.setText("*Telephone Number is Empty ");
                }

                //.............
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                String email = txtEmail.getText();
                String telephoneNumber = txtTelephoneNumber.getText();

                //..........

            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //...............

            }
        });

        createTable();


    }

    private void createTable() {
        final String[] columnNames = {"First Name","Last Name","Email","Telephone Number"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames,0);
        try {
            for (var e:dataRepository.get(BookAuthorModel.class) ) {
              BookAuthorModel data = (BookAuthorModel) e;
              tableModel.addRow(new String[]{data.getFirstName(),data.getFirstName(),data.getEmail(),data.getPhone()});
            }
        } catch (DataRepositoryException e) {
            e.printStackTrace();
        }
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);
    }
}
