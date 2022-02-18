

package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.models.BookPublisherModel;
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

public class Publisher extends PageView {
    final private DataRepository dataRepository;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JTextField txtFirstName;
    private JTextField txtEmail;
    private JTextField txtTelephoneNumber;
    private JButton btnSave;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JScrollPane jScrollPane;
    private JTable table;

    private List<SqlDataModel> dataList;
    private BookPublisherModel bookPublisherModel;


    public Publisher(DataRepository dataRepository) {
        super();

        this.dataRepository = dataRepository;
        dataRepository.createStatement("book_publisher");
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

        if (bookPublisherModel == null)
            bookPublisherModel = new BookPublisherModel();

        bookPublisherModel.setName(txtFirstName.getText());
        bookPublisherModel.setEmail(txtEmail.getText());
        bookPublisherModel.setPhone(txtTelephoneNumber.getText());

        if (bookPublisherModel.getName().isEmpty()) {
            Alert.showError("Validation Error:", "Name is Empty!");
            return;
        }
        if (bookPublisherModel.getEmail().isEmpty()) {
            Alert.showError("Validation Error:", "Email is Empty!");
            return;
        }
        if (bookPublisherModel.getPhone().isEmpty()) {
            Alert.showError("Validation Error:", "Telephone Number is Empty!");
            return;
        }

        try {

            dataRepository.createStatement("book_publisher");
            if (bookPublisherModel.getId() == 0) {
                dataRepository.insert(bookPublisherModel);
            } else {
                dataRepository.where("id", bookPublisherModel.getId());
                dataRepository.update(bookPublisherModel);
            }

            clearFields();
            createTable();
            bookPublisherModel = null;
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

        bookPublisherModel = (BookPublisherModel) dataList.get(selectedRow);

        txtFirstName.setText(bookPublisherModel.getName());
        txtEmail.setText(bookPublisherModel.getEmail());
        txtTelephoneNumber.setText(bookPublisherModel.getPhone());

    }

    private void delete() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Alert.showError("Deletion Error:", "Please select the record that you want to delete!");
            return;
        }

        final int response = Alert.showConfirm("Delete record", "Are you sure you want to delete?");
        bookPublisherModel = (BookPublisherModel) dataList.get(selectedRow);
        bookPublisherModel.setIsDelete(1);

        if (response == 0) {

            try {

                dataRepository.createStatement();
                dataRepository.where("id", bookPublisherModel.getId());
                dataRepository.update(bookPublisherModel);
                bookPublisherModel = null;
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
        bookPublisherModel = null;
    }

    private void createTable() {
        final String[] columnNames = {"Name", "Email", "Telephone Number"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            dataRepository.createStatement();
            dataRepository.where("is_delete", 0);
            dataList = dataRepository.get(BookPublisherModel.class);

            for (var row : dataList) {
                BookPublisherModel data = (BookPublisherModel) row;
                tableModel.addRow(new String[]{data.getName(), data.getEmail(), data.getPhone()});
            }

        } catch (DataRepositoryException e) {

            Alert.showError("Data loading error:", e.getMessage());

        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);

    }
}
