package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.models.CategoryModel;
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

public class Category extends PageView {
    private JPanel mainPanel;
    private JScrollPane JScrollPaneAdd;
    private JTextField txtCategory;
    private JTextField txtSubCategory;
    private JScrollPane jScrollPane;
    private JButton btnAdd;
    private JButton btnClear;
    private JButton btnSave;
    private JButton btnDelete;
    private JButton btnEdit;

    private DataRepository dataRepository;
    private List<SqlDataModel> dataList;
    private CategoryModel categoryModel;
    private JTable table;

    public Category(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        try {
            dataRepository.executeQuery("SELECT category,book_category.id,\n" +
                    "        IF NULL(GROUP_CONCAT(sub_category),'N/A') AS 'sub_category' \n" +
                    "        FROM book_category \n" +
                    "        LEFT JOIN book_sub_category \n" +
                    "        ON book_sub_category.category_id = book_category.id AND book_sub_category.is_delete = 0\n" +
                    "        WHERE book_category.is_delete = 0 \n" +
                    "        GROUP BY book_category.id");
        } catch (DataRepositoryException e) {
            e.printStackTrace();
        }
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);

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
        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addSubCategory();
            }
        });

        createTable();
    }

    private void insert() {
        if (categoryModel == null)
            categoryModel = new CategoryModel();

        categoryModel.setCategory(txtCategory.getText());
        categoryModel.setCategory(txtSubCategory.getText());

        if (categoryModel.getCategory().isEmpty()){
            Alert.showError("Validation Error:", "Category is Empty!");
        }
        if (categoryModel.getSubCategory().isEmpty()){
            Alert.showError("Validation Error:", "Sub Category is Empty!");
        }
    }

    private void edit() {

    }

    private void delete() {

    }

    private void clearFields() {

    }

    private void addSubCategory() {

    }

    private void createTable() {
        final String[] columnNames = {"category", "sub_category"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            dataRepository.executeQuery("SELECT category,book_category.id,\n" +
                    "        IF NULL(GROUP_CONCAT(sub_category),'N/A') AS 'sub_category' \n" +
                    "        FROM book_category \n" +
                    "        LEFT JOIN book_sub_category \n" +
                    "        ON book_sub_category.category_id = book_category.id AND book_sub_category.is_delete = 0\n" +
                    "        WHERE book_category.is_delete = 0 \n" +
                    "        GROUP BY book_category.id");
            dataList = dataRepository.get(CategoryModel.class);

            for (var row: dataList){
                CategoryModel data = (CategoryModel) row;
                tableModel.addRow(new String[]{data.getCategory(), data.getSubCategory()});
            }
        } catch (DataRepositoryException e) {
            e.printStackTrace();
        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);
    }

}
