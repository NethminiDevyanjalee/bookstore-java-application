package com.debuggers.bookstore.views;

import javax.swing.*;

public class Alert {

    static public void showError(String title,String message) {
        JOptionPane.showMessageDialog(new JFrame(),message,title,JOptionPane.ERROR_MESSAGE);
    }

    static public void showSuccess(String title,String message) {
        JOptionPane.showMessageDialog(new JFrame(),message,title,JOptionPane.INFORMATION_MESSAGE);
    }


}
