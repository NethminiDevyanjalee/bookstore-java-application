package com.debuggers.bookstore.views;

import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.views.book.Author;

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
    }

    private void initComponents() {
        cards = new JPanel(new CardLayout());
        cards.add(new Home(dataRepository),"home");
        cards.add(new Author(dataRepository),"author");
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
