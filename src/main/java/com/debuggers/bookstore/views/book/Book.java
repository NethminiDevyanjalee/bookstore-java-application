package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.models.BookAuthorModel;
import com.debuggers.bookstore.models.BookModel;
import com.debuggers.bookstore.models.SqlDataModel;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;
import com.debuggers.bookstore.views.Alert;
import com.debuggers.bookstore.views.PageView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Book extends PageView {
    private DataRepository dataRepository;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JTextField txtName;
    private JTextField txtISBN;
    private JTextField txtPrice;
    private JButton btnSave;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JTable table;
    private JScrollPane jScrollPane;
    private JComboBox comboLanguage;
    private JComboBox comboAuthor;
    private JComboBox comboPublisher;
    private JComboBox comboCategory;
    private JComboBox comboSubCategory;
    private JTextArea textareaDescription;

    private List<SqlDataModel> dataList;
    private List<SqlDataModel> authorList;
    private BookModel bookModel;

    public Book(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        dataRepository.createStatement("book");
        initComponents();

        btnClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //clearFields();
            }
        });
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //delete();
            }
        });
        btnEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //edit();
            }
        });
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //insert();
            }
        });

        createTable();
    }

    private void initComponents() {
        add(mainPanel);
        loadAuthor();
    }

    private void loadAuthor() {

        dataRepository.createStatement("book_author");
        dataRepository.where("is_delete",0);

        try {

            authorList = dataRepository.get(BookAuthorModel.class);
            comboAuthor.addItem("Choose");
            authorList.stream().forEach((e)->{
                BookAuthorModel author = (BookAuthorModel) e;
                comboAuthor.addItem(author.getFirstName()+" "+author.getLastName());
            });

        } catch (DataRepositoryException e) {

            Alert.showError("Database Error:",e.getMessage());

        }
    }

    private void insert() {

        if (bookModel == null)
            bookModel = new BookModel();

        bookModel.setName(txtName.getText());
        BookAuthorModel selectedAuthor = (BookAuthorModel) authorList.get(comboAuthor.getSelectedIndex());
        if (comboAuthor.getSelectedIndex() == 0) {
            Alert.showError("Validation Error:", "Select the Author!");
            return;
        }
    }

    private void createTable() {
        final String[] columnNames = {"Name", "ISBN", "Email", "Author", "Publisher", "Language", "Price"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);
    }
}

