package com.debuggers.bookstore.views;

import com.debuggers.bookstore.repository.DataRepository;
import com.googlecode.charts4j.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class Home extends PageView{
    private JPanel mainPanel;
    private JPanel mainChart;

    private DataRepository dataRepository;

    public Home(DataRepository dataRepository) {
        super();
        this.dataRepository = dataRepository;
        draw();
        initComponents();
    }

    private void initComponents() {
        add(mainPanel);
    }

    public String drawLineGraph() {
        // Your really great chart.
        final Plot plot = Plots.newPlot(Data.newData(0, 10.6, 20.5, 80.20, 50.50, 95.5, 92.00));
        final LineChart chart = GCharts.newLineChart(plot);
        chart.setTitle("Growth of Alibata System Inc. (Estimated Plot)");
        chart.setSize(720, 360);
        return chart.toURLString();
    }

    private void draw() {;
        JLabel label = null;
        try {
            label = new JLabel(new ImageIcon(ImageIO.read(new URL(drawLineGraph()))));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        mainChart.setLayout(new BoxLayout(mainChart, BoxLayout.PAGE_AXIS));
        mainChart.add(label);
    }
}
