package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.models.BookModel;
import com.debuggers.bookstore.models.BookStockModel;
import com.debuggers.bookstore.models.SqlDataModel;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.views.PageView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Stock extends PageView {
    final private DataRepository dataRepository;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JButton btnSave;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JSpinner spinnerQuantity;
    private JComboBox comboName;
    private JTable table;
    private JScrollPane jScrollPane;

    private List<SqlDataModel> dataList;
    private BookStockModel bookStockModel;

    public Stock(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        dataRepository.createStatement("book_stock");
        initComponents();
        btnClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        btnEdit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });

        createTable();
    }

    private void initComponents() {
        add(mainPanel);
    }

    private void createTable() {
        final String[] columnNames = {"Name", "ISBN", "Status", "Quantity"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);
    }
}
