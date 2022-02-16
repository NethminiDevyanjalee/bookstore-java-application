package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.models.BookAuthorModel;
import com.debuggers.bookstore.models.SqlDataModel;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;
import com.debuggers.bookstore.views.Alert;
import com.debuggers.bookstore.views.PageView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
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
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JPanel formPanel;

    private DataRepository dataRepository;
    private List<SqlDataModel> dataList;
    private BookAuthorModel bookAuthorModel;

    public Author(DataRepository dataRepository) {
        super();

        this.dataRepository = dataRepository;
        dataRepository.createStatement("book_author");
        initComponents();

        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                insert();
            }
        });
        btnEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                edit();
            }
        });
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                delete();
            }

        });
        btnClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clearFields();
            }
        });

        createTable();
    }

    private void initComponents() {
        add(mainPanel);
    }

    private void insert() {

        if (bookAuthorModel == null)
            bookAuthorModel = new BookAuthorModel();

        bookAuthorModel.setFirstName(txtFirstName.getText());
        bookAuthorModel.setLastName(txtLastName.getText());
        bookAuthorModel.setEmail(txtEmail.getText());
        bookAuthorModel.setPhone(txtTelephoneNumber.getText());

        if (bookAuthorModel.getFirstName().isEmpty()) {
            Alert.showError("Validation Error:", "First Name is Empty!");
            return;
        }
        if (bookAuthorModel.getLastName().isEmpty()) {
            Alert.showError("Validation Error:", "Last Name is Empty!");
            return;
        }
        if (bookAuthorModel.getEmail().isEmpty()) {
            Alert.showError("Validation Error:", "Email is Empty!");
            return;
        }
        if (bookAuthorModel.getPhone().isEmpty()) {
            Alert.showError("Validation Error:", "Telephone Number is Empty!");
            return;
        }

        try {

            dataRepository.createStatement();
            System.out.println(bookAuthorModel.getId());
            if (bookAuthorModel.getId() == 0) {
                dataRepository.insert(bookAuthorModel);
            } else {
                dataRepository.where("id", bookAuthorModel.getId());
                dataRepository.update(bookAuthorModel);
            }

            clearFields();
            createTable();
            bookAuthorModel = null;
            Alert.showSuccess("Success", "This change has been saved!");

        } catch (DataRepositoryException exception) {

            Alert.showError("Database error:", exception.getMessage());

        }

    }

    private void edit() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Alert.showError("Edit Error:", "Please select the record that you want to edit!");
            return;
        }

        bookAuthorModel = (BookAuthorModel) dataList.get(selectedRow);

        txtFirstName.setText(bookAuthorModel.getFirstName());
        txtLastName.setText(bookAuthorModel.getLastName());
        txtEmail.setText(bookAuthorModel.getEmail());
        txtTelephoneNumber.setText(bookAuthorModel.getPhone());

    }

    private void delete() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Alert.showError("Deletion Error:", "Please select the record that you want to delete!");
            return;
        }

        final int response = Alert.showConfirm("Delete record", "Are you sure you want to delete?");
        bookAuthorModel = (BookAuthorModel) dataList.get(selectedRow);
        bookAuthorModel.setIsDelete(1);

        if (response == 0) {

            try {

                dataRepository.createStatement();
                dataRepository.where("id", bookAuthorModel.getId());
                dataRepository.update(bookAuthorModel);
                bookAuthorModel = null;
                createTable();

            } catch (DataRepositoryException exception) {

                Alert.showError("Deletion Error:", exception.getMessage());

            }
        }

    }


    private void clearFields() {
        Arrays.stream(formPanel.getComponents()).forEach((c) -> {
            if (c instanceof JTextField) {
                ((JTextField) c).setText(null);
            }
        });
        bookAuthorModel = null;
    }

    private void createTable() {
        final String[] columnNames = {"First Name", "Last Name", "Email", "Telephone Number"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            dataRepository.createStatement();
            dataRepository.where("is_delete", 0);
            dataList = dataRepository.get(BookAuthorModel.class);

            for (var row : dataList) {
                BookAuthorModel data = (BookAuthorModel) row;
                tableModel.addRow(new String[]{data.getFirstName(), data.getLastName(), data.getEmail(), data.getPhone()});
            }

        } catch (DataRepositoryException e) {

            Alert.showError("Data loading error:", e.getMessage());

        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);

    }
}
