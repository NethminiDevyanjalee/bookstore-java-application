package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.models.BookStockModel;
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

public class Stock extends PageView {
    final private DataRepository dataRepository;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JSpinner txtQty;
    private JButton btnAdd;
    private JTable table;
    private JScrollPane jScrollPane;

    private List<SqlDataModel> dataList;
    private BookStockModel bookStockModel;

    private String fetchQuery = """
            SELECT book.isbn,book.name,book.id,
            IFNULL((SELECT (SUM(in_qty) -SUM(out_qty)) FROM book_stock WHERE book_id = book.id),0) AS qty
            FROM book
            WHERE is_delete = 0
            """;
    private String addStockQuery = """
            INSERT INTO book_stock (book_id,trans_code,trans_id,in_qty)
            VALUES (%s,'ADD-STOCK',0,%s)
            """;

    public Stock(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
        createTable();
        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                add();
            }
        });
    }

    private void add() {
        final int selectedRow = table.getSelectedRow();
        final int qty = (Integer) txtQty.getValue();

        if (selectedRow == -1) {
            Alert.showError("Selection Error:", "Please select the book that you want to add new stock!");
            return;
        }
        if (qty == 0) {
            Alert.showError("Validation Error:", "Please add quantities");
            return;
        }
        bookStockModel = (BookStockModel) dataList.get(selectedRow);

        try {
            dataRepository.execute(addStockQuery.formatted(String.valueOf(bookStockModel.getId()), String.valueOf(qty)));
            Alert.showSuccess("Success", "This change has been saved!");
            createTable();
            clearFields();
        } catch (DataRepositoryException exception) {
            Alert.showError("Database Error:", exception.getMessage());
        }


    }

    private void createTable() {
        final String[] columnNames = {"Name", "ISBN", "Status", "Quantity"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        try {
            dataList = dataRepository.executeQuery(fetchQuery, BookStockModel.class);
            dataList.forEach((e) -> {
                final BookStockModel data = (BookStockModel) e;
                tableModel.addRow(new String[]{
                        data.getName(),
                        data.getIsbn(),
                        data.getQuantity() > 0 ? "IN STOCK" : "OUT OF STOCk",
                        String.valueOf(data.getQuantity())
                });
            });
        } catch (DataRepositoryException exception) {
            Alert.showError("Database Error:", exception.getMessage());
        }
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);
    }

    private void clearFields() {
        txtQty.setValue(0);
        bookStockModel = null;
    }
}
