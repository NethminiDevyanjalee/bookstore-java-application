package com.debuggers.bookstore.views;

import com.debuggers.bookstore.models.PromotionModel;
import com.debuggers.bookstore.models.SqlDataModel;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class Promotion extends PageView {
    private JPanel mainPanel;
    private JTextField txtCouponCode;
    private JTextField txtFrom;
    private JTextField txtDiscount;
    private JTextField txtTo;
    private JTable table;
    private JButton btnSave;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JScrollPane jScrollPane;

    private DataRepository dataRepository;
    private List<SqlDataModel> dataList;
    private PromotionModel promotionModel;

    public Promotion(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        dataRepository.createStatement("coupon_code");

        initComponents();
    }

    private void initComponents(){
        add(mainPanel);

        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
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

    private void insert() {
        if (promotionModel == null)
            promotionModel = new PromotionModel();

        promotionModel.setCouponCode(txtCouponCode.getText());
        promotionModel.setDiscount(Float.parseFloat(txtDiscount.getText()));
        promotionModel.setFrom(Date.valueOf(txtFrom.getText()));
        promotionModel.setTo(Date.valueOf(txtTo.getText()));

        if(promotionModel.getCouponCode().isEmpty()){
            Alert.showError("Validation Error:", "Coupon Code is Empty");
            return;
        }
        if (String.valueOf(promotionModel.getDiscount()).isEmpty()){
            Alert.showError("Validation Error:", "Discount is Empty");
            return;
        }
        if (String.valueOf(promotionModel.getFrom()).isEmpty()){
            Alert.showError("Validation Error:", "Starting Date is Empty");
            return;
        }
        if (String.valueOf(promotionModel.getTo()).isEmpty()){
            Alert.showError("Validation Error:", "Ending Date is Empty");
            return;
        }

        try {
            dataRepository.createStatement();
            System.out.println(promotionModel.getId());
            if (promotionModel.getId() == 0){
                dataRepository.insert(promotionModel);
            }else {
                dataRepository.where("id", promotionModel.getId());
                dataRepository.update(promotionModel);
            }

            clearFields();
            createTable();
            promotionModel = null;
            Alert.showSuccess("Success", "This change has been save!");
        } catch (DataRepositoryException e) {
            Alert.showError("Database Error", e.getMessage());
        }
    }

    private void edit() {
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1){
            Alert.showError("Edit Error", "Select The record that you want edit!");
            return;
        }

        promotionModel = (PromotionModel) dataList.get(selectedRow);

        txtCouponCode.setText(promotionModel.getCouponCode());
        txtDiscount.setText(String.valueOf(promotionModel.getDiscount()));
        txtFrom.setText(String.valueOf(promotionModel.getFrom()));
        txtTo.setText(String.valueOf(promotionModel.getTo()));
    }

    private void delete(){
        final int selectedRow = table.getSelectedRow();

        if (selectedRow == -1){
            Alert.showError("Delete Error", "Select the record that you want to delete!");
            return;
        }

        final int response = Alert.showConfirm("Delete record", "Are you sure you want to delete");
        promotionModel = (PromotionModel) dataList.get(selectedRow);
        promotionModel.setIsDelete(1);

        if (response == 0){
            try {
                dataRepository.createStatement();
                dataRepository.where("id", promotionModel.getId());
                dataRepository.update(promotionModel);
                promotionModel = null;
                createTable();
            } catch (DataRepositoryException e){
                Alert.showError("Deletion Error", e.getMessage());
            }
        }
    }

    private void clearFields(){
        Arrays.stream(mainPanel.getComponents()).forEach((c)->{
            if (c instanceof JTextField){
                ((JTextField) c).setText(null);
            }
        });
        promotionModel = null;
    }

    private void createTable() {
        final String[] columnNames = {"code", "discount", "from", "to"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            dataRepository.createStatement();
            dataRepository.where("is_delete", 0);
            dataList = dataRepository.get(PromotionModel.class);

            for (var row : dataList){
                PromotionModel data = (PromotionModel) row;
                tableModel.addRow(new String[]{data.getCouponCode(), String.valueOf(data.getDiscount()), String.valueOf(data.getFrom()), String.valueOf(data.getTo())});
            }
        } catch (DataRepositoryException e) {
            Alert.showError("Data loading error:", e.getMessage());
        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane.setViewportView(table);
    }

}
