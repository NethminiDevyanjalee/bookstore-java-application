package com.debuggers.bookstore.views;

import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;
import com.googlecode.charts4j.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Home extends PageView {
    private JPanel mainPanel;
    private JPanel chartPanel1;
    private JPanel chartPanel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;


    private final String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private final String monthlySalesQuery = "SELECT `created_at` ,SUM(IFNULL((SELECT SUM(qty * price) FROM `order_item` WHERE `order_item`.`order_id` = `order`.id),0)) AS amount FROM `order` WHERE YEAR(`created_at`) = _YEAR_ AND MONTH(`created_at`) = _MONTH_";
    private final String dailyOrderCountQuery = "SELECT COUNT(id)AS `count` FROM `order` WHERE DATE(`created_at`) = '_DATE_'";
    private final String recentOrdersQuery = "SELECT `order`.* ,(SELECT SUM(out_amount) FROM `payment` WHERE trans_code='PURCHASE' AND trans_id = `order`.id) AS price FROM `order` ORDER BY `created_at` DESC LIMIT 10";
    private String mostSalesBookQuery = """
            SELECT book.`name`,
            CONCAT(book_author.fname,' ',book_author.lname) AS author,
            book_publisher.name AS publisher,
            book_language.language,
            (SELECT COUNT(`book_id`) FROM `order_item` WHERE `book_id` = `book`.`id`) AS total_sales
            FROM `book`
            INNER JOIN book_author ON book_author.id = book.author_id
            INNER JOIN book_publisher ON book_publisher.id = book.publisher_id
            INNER JOIN book_language ON book_language.id = book.language_id
            WHERE book.is_delete = 0
            ORDER BY total_sales DESC
            LIMIT 10
            """;

    private DataRepository dataRepository;
    private JTable table;

    public Home(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
        drawMonthlySalesChart();
        drawDailyOrdersCountChart();
        createTable();
    }

    private void drawMonthlySalesChart() {
        List<Double> data = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();
        List<String> yLabels = new ArrayList<>();

        for (int i = 0; i < months.length; i++) {
            data.add(getMonthlySales(2021, i + 1) / 1000);
            xLabels.add(months[i]);
        }

        for (int i = 0; i < 10; i++)
            yLabels.add(String.valueOf(i * 1000));

        final Plot plot = Plots.newPlot(Data.newData(data));
        final LineChart chart = GCharts.newLineChart(plot);
        chart.setTitle("Monthly Sales");
        chart.addXAxisLabels(AxisLabelsFactory.newAxisLabels(xLabels));
        chart.addYAxisLabels(AxisLabelsFactory.newAxisLabels(yLabels));
        chart.addHorizontalRangeMarker(0, 100, Color.ALICEBLUE);

        chart.setSparkline(true);
        chart.setSize(340, 170);

        JLabel label = null;

        try {
            label = new JLabel(new ImageIcon(ImageIO.read(new URL(chart.toURLString()))));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        chartPanel1.setLayout(new BoxLayout(chartPanel1, BoxLayout.PAGE_AXIS));
        chartPanel1.add(label);

    }

    private void drawDailyOrdersCountChart() {

        List<Double> data = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();
        List<String> yLabels = new ArrayList<>();

        LocalDate now = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            final LocalDate date = now.minusDays(i);
            xLabels.add(date.getDayOfWeek().toString().substring(0, 3).toLowerCase());
            data.add(getDailyOrdersCount(date.toString()) * 10);
        }

        for (int i = 0; i < 10; i++)
            yLabels.add(String.valueOf(i));

        final Plot plot = Plots.newPlot(Data.newData(data));
        final BarChart chart = GCharts.newBarChart(plot);
        chart.setTitle("Daily Sales");
        chart.addXAxisLabels(AxisLabelsFactory.newAxisLabels(xLabels));
        chart.addYAxisLabels(AxisLabelsFactory.newAxisLabels(yLabels));

        //chart.setSparkline(true);
        chart.setBarWidth(5);
        chart.setSpaceBetweenGroupsOfBars(40);
        chart.setSize(340, 170);

        JLabel label = null;

        try {
            label = new JLabel(new ImageIcon(ImageIO.read(new URL(chart.toURLString()))));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        chartPanel2.setLayout(new BoxLayout(chartPanel2, BoxLayout.PAGE_AXIS));
        chartPanel2.add(label);

    }

    private double getMonthlySales(int year, int month) {
        double amount = 0;

        try {

            String sql = monthlySalesQuery
                    .replaceAll("_YEAR_", String.valueOf(year))
                    .replaceAll("_MONTH_", String.valueOf(month));
            final ResultSet resultSet = dataRepository.executeQuery(sql);
            resultSet.next();
            amount = resultSet.getDouble("amount");

        } catch (DataRepositoryException | SQLException e) {

            Alert.showError("Database Error:", e.getMessage());

        }

        return amount;
    }

    private double getDailyOrdersCount(String date) {
        double count = 0;

        try {

            String sql = dailyOrderCountQuery
                    .replaceAll("_DATE_", String.valueOf(date));
            final ResultSet resultSet = dataRepository.executeQuery(sql);
            resultSet.next();
            count = resultSet.getDouble("count");

        } catch (DataRepositoryException | SQLException e) {

            Alert.showError("Database Error:", e.getMessage());

        }

        return count;
    }

    private void createTable() {
        createRecentOrderTable();
        createMostSalesTable();
    }

    private void createRecentOrderTable() {
        final String[] columnNames = {"ID", "Date", "City", "Amount"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            ResultSet resultSet = dataRepository.executeQuery(recentOrdersQuery);
            while (resultSet.next()) {
                tableModel.addRow(new String[]{resultSet.getString("id"), resultSet.getString("created_at"), resultSet.getString("city"), resultSet.getString("price")});
            }


        } catch (DataRepositoryException | SQLException e) {

            Alert.showError("Data loading error:", e.getMessage());

        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane1.setViewportView(table);

    }

    private void createMostSalesTable() {
        final String[] columnNames = {"Book", "Author", "Publisher", "Language", "Total Sales"};
        final DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        try {
            ResultSet resultSet = dataRepository.executeQuery(mostSalesBookQuery);
            while (resultSet.next()) {
                tableModel.addRow(new String[]{resultSet.getString("name"), resultSet.getString("author"), resultSet.getString("publisher"), resultSet.getString("language"), resultSet.getString("total_sales")});
            }


        } catch (DataRepositoryException | SQLException e) {

            Alert.showError("Data loading error:", e.getMessage());

        }

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        jScrollPane2.setViewportView(table);

    }
}
