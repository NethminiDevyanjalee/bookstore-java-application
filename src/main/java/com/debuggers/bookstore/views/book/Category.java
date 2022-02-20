package com.debuggers.bookstore.views.book;

import com.debuggers.bookstore.models.BookAuthorModel;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Category extends PageView {
    private JPanel mainPanel;
    private JTextField txtCategory;
    private JTextField txtSubCategory;
    private JScrollPane jScrollPane;
    private JButton btnAdd;
    private JButton btnClear;
    private JButton btnSave;
    private JButton btnDelete;
    private JButton btnEdit;
    private JList subCatList;
    private JTable table;

    private DataRepository dataRepository;
    private List<SqlDataModel> dataList;
    private CategoryModel categoryModel;
    private String fetchQuery = """
            SELECT category,book_category.id,book_category.is_delete,
            IFNULL(GROUP_CONCAT(sub_category),'N/A') AS 'sub_category',
            IFNULL(GROUP_CONCAT(book_sub_category.id),'N/A') AS 'sub_category_ids'
            FROM book_category
            LEFT JOIN book_sub_category
            ON book_sub_category.category_id = book_category.id AND book_sub_category.is_delete = 0
            WHERE book_category.is_delete = 0
            GROUP BY book_category.id
            """;
    private String subCatInsertQuery = "INSERT INTO book_sub_category(category_id,sub_category) VALUES(%o,'%s')";

    public Category(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
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

        if (categoryModel.getCategory().isEmpty()) {
            Alert.showError("Validation Error:", "Category is Empty!");
            return;
        }

        try {
            int insertID;
            dataRepository.createStatement("book_category");
            if (categoryModel.getId() == 0) {
                insertID = (int) dataRepository.insert(categoryModel);
            } else {
                insertID = categoryModel.getId();
                dataRepository.where("id", categoryModel.getId());
                dataRepository.update(categoryModel);
            }

            insertSubCat(insertID);

            clearFields();
            createTable();
            categoryModel = null;
            Alert.showSuccess("Success", "This change has been save!");

        } catch (DataRepositoryException exception) {

            Alert.showError("Database error:", exception.getMessage());

        }

    }

    private void insertSubCat(int insertID) {
        if (subCatList.getModel().getSize() == 0) return;
        dataRepository.createStatement("book_sub_category");
        dataRepository.where("category_id", insertID);

        try {

            dataRepository.delete();

            for (int i = 0; i < subCatList.getModel().getSize(); i++) {
                dataRepository.execute(subCatInsertQuery.formatted(insertID, subCatList.getModel().getElementAt(i)));
            }

        } catch (DataRepositoryException exception) {
            Alert.showError("Database error:", exception.getMessage());
        }
    }

    private void edit() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Alert.showError("Edit Error:", "Please the record that you want to edit!");
            return;
        }
        categoryModel = (CategoryModel) dataList.get(selectedRow);
        txtCategory.setText(categoryModel.getCategory());
        subCatList.setListData(categoryModel.getSubCategoriesList().toArray());
    }

    private void delete() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Alert.showError("Deletion Error:", "Please the record that you want to delete!");
            return;
        }

        final int response = Alert.showConfirm("Delete record", "Are you sure you want to delete");
        categoryModel = (CategoryModel) dataList.get(selectedRow);
        categoryModel.setIsDelete(1);

        if (response == 0) {

            try {

                dataRepository.createStatement("book_category");
                dataRepository.where("id", categoryModel.getId());
                dataRepository.update(categoryModel);
                categoryModel = null;
                createTable();

            } catch (DataRepositoryException exception) {

                Alert.showError("Deletion Error:", exception.getMessage());

            }
        }

    }

    private void clearFields() {
        Arrays.stream(mainPanel.getComponents()).forEach((c) -> {
            if (c instanceof JTextField) {
                ((JTextField) c).setText(null);
            }
        });
        subCatList.setListData(new Object[]{});
        categoryModel = null;
    }

    private void addSubCategory() {
        List<Object> data = new ArrayList<>();
        String subCat = txtSubCategory.getText();

        if (subCat.isEmpty()) {
            Alert.showError("Validation Error:", "Sub Category name is required.");
            return;
        }

        for (int i = 0; i < subCatList.getModel().getSize(); i++) {
            data.add(subCatList.getModel().getElementAt(i));
        }

        data.add(subCat);
        subCatList.setListData(data.toArray());
        txtSubCategory.setText(null);
    }

    private void createTable() {
        final String[] columnNames = {"Category", "Sub Categories"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {

            dataList = dataRepository.executeQuery(fetchQuery, CategoryModel.class);

            for (var row : dataList) {
                CategoryModel data = (CategoryModel) row;
                tableModel.addRow(new String[]{data.getCategory(), data.getSubCategoriesStr()});
            }
        } catch (DataRepositoryException e) {
            Alert.showError("Database Error:", e.getMessage());
        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);
    }

}
