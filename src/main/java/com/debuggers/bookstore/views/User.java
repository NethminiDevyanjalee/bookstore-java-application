package com.debuggers.bookstore.views;

import com.debuggers.bookstore.models.*;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class User extends PageView{
    final private DataRepository dataRepository;

    private JPanel mainPanel;
    private JScrollPane jScrollPane;
    private JPanel formPanel;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JButton btnSave;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JComboBox comboBox1;
    private JTextField txtPassword;
    private JTable table;

    private List<SqlDataModel> dataList;
    private List<SqlDataModel> userRoleList;
    private BookUserModel bookUserModel;

    public User(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();

        btnClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                clearFields();
            }
        });
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                delete();
            }
        });
        btnEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                edit();
            }
        });
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                insert();
            }
        });

        createTable();
    }

    private void initComponents() {
        add(mainPanel);
        loadUserRole();
    }

    private void loadUserRole() {

        dataRepository.createStatement("system_user_role");

        try {

            userRoleList = dataRepository.get(BookUserRoleModel.class);
            comboBox1.addItem("Choose");
            userRoleList.stream().forEach((e)->{
                BookUserRoleModel user = (BookUserRoleModel) e;
                comboBox1.addItem(user.getUserRole());
            });

        } catch (DataRepositoryException e) {

            Alert.showError("Database Error:",e.getMessage());

        }
    }

    private void insert() {

        if (bookUserModel == null)
            bookUserModel = new BookUserModel();

        bookUserModel.setFirstName(txtFirstName.getText());
        bookUserModel.setLastName(txtLastName.getText());
        bookUserModel.setEmail(txtEmail.getText());
        bookUserModel.setPassword(getMD5(txtPassword.getText()));

        if (bookUserModel.getFirstName().isEmpty()) {
            Alert.showError("Validation Error:", "First Name is Empty!");
            return;
        }
        if (bookUserModel.getLastName().isEmpty()) {
            Alert.showError("Validation Error:", "Last Name is Empty!");
            return;
        }
        if (bookUserModel.getEmail().isEmpty()) {
            Alert.showError("Validation Error:", "Email is Empty!");
            return;
        }
        if (bookUserModel.getPassword().isEmpty()) {
            Alert.showError("Validation Error:", "Password is Empty!");
            return;
        }


        try {

            dataRepository.createStatement("system_user");
            if (bookUserModel.getId() == 0) {
                dataRepository.insert(bookUserModel);
            } else {
                dataRepository.where("id", bookUserModel.getId());
                dataRepository.update(bookUserModel);
            }

            clearFields();
            createTable();
            bookUserModel = null;
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

        bookUserModel = (BookUserModel) dataList.get(selectedRow);

        txtFirstName.setText(bookUserModel.getFirstName());
        txtLastName.setText(bookUserModel.getLastName());
        txtEmail.setText(bookUserModel.getEmail());

    }

    private void delete() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Alert.showError("Deletion Error:", "Please select the record that you want to delete!");
            return;
        }

        final int response = Alert.showConfirm("Delete record", "Are you sure you want to delete?");
        bookUserModel = (BookUserModel) dataList.get(selectedRow);
        bookUserModel.setIsDelete(1);

        if (response == 0) {

            try {

                dataRepository.createStatement();
                dataRepository.where("id", bookUserModel.getId());
                dataRepository.update(bookUserModel);
                bookUserModel = null;
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
        bookUserModel = null;
    }

    private void createTable() {
        final String[] columnNames = {"First Name", "Last Name", "Email"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {

            dataRepository.createStatement("system_user");
            dataRepository.where("is_delete", 0);
            dataList = dataRepository.get(BookUserModel.class);

            for (var row : dataList) {
                BookUserModel data = (BookUserModel) row;
                tableModel.addRow(new String[]{data.getFirstName(), data.getLastName(), data.getEmail()});
            }

        } catch (DataRepositoryException e) {

            Alert.showError("Data loading error:", e.getMessage());

        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);

    }

    private String getMD5(String str) {
        MessageDigest md = null;
        String hashText = null;
        try {
            md = MessageDigest.getInstance("MD5");
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(str.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

        } catch (NoSuchAlgorithmException e) {
            Alert.showError("System Error", e.getMessage());
        }

        return hashText;

    }
}