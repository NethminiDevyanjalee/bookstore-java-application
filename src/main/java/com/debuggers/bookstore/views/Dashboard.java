package com.debuggers.bookstore.views;

import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.views.book.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Dashboard extends View{

    private JPanel mainPanel;
    private JPanel navPanel;
    private JButton homeButton;
    private JButton bookAuthorsButton;
    private JPanel pageViewPanel;
    private JButton bookPublisherButton;
    private JButton bookCategoryButton;
    private JButton bookButton;
    private JButton stockButton;
    private JButton promationButton;
    private JButton usersButton;
    private JPanel cards;

    private DataRepository dataRepository;

    public Dashboard(DataRepository dataRepository) {
        super();

        this.dataRepository = dataRepository;

        initComponents();

        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                navigateTo("home");
            }
        });
        bookAuthorsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                navigateTo("author");
            }
        });
        bookPublisherButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                navigateTo("publisher");
            }
        });
        bookCategoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                navigateTo("category");
            }
        });
        bookButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                navigateTo("book");
            }
        });
        stockButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                navigateTo("stock");
            }
        });
        promationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                navigateTo("promotion");
            }
        });
        usersButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                navigateTo("user");
            }
        });
    }

    private void initComponents() {
        cards = new JPanel(new CardLayout());

        // Set up navigations
        cards.add(new Home(dataRepository),"home");
        cards.add(new Author(dataRepository),"author");
        cards.add(new Publisher(dataRepository),"publisher");
        cards.add(new Category(dataRepository),"category");
        cards.add(new Book(dataRepository),"book");
        cards.add(new Stock(dataRepository),"stock");
        cards.add(new Promotion(dataRepository),"promotion");
        cards.add(new User(dataRepository),"user");

        pageViewPanel.setLayout(new BoxLayout(pageViewPanel, BoxLayout.PAGE_AXIS));
        pageViewPanel.add(cards);
        setContentPane(mainPanel);
        setVisible(true);
    }

    private void navigateTo(String page) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards,page);
    }

}
