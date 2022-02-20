package com.debuggers.bookstore.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class View extends JFrame {

    public View() {
        setTitle("BOOKBERRIES SYSTEM");
        try {
            setIconImage(ImageIO.read(new File("src/main/resources/images/favicon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSize(1080, 720);
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        setLocation(dx, dy);
    }

}

