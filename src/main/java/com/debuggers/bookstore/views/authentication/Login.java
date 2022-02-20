package com.debuggers.bookstore.views.authentication;

import com.debuggers.bookstore.models.SqlDataModel;
import com.debuggers.bookstore.models.UserModel;
import com.debuggers.bookstore.repository.DataRepository;
import com.debuggers.bookstore.repository.DataRepositoryException;
import com.debuggers.bookstore.views.Alert;
import com.debuggers.bookstore.views.Dashboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Login extends JFrame {
    private DataRepository dataRepository;
    private JPanel mainPanel;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel imageLabel;

    public Login(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        dataRepository.createStatement("system_user");
        initComponents();
    }

    private void initComponents() {
        setContentPane(mainPanel);
        setSize(720, 540);
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        setLocation(dx, dy);
        setTitle("BOOKBERRIES");
        setVisible(true);

        try {
            imageLabel.setIcon(
                    new ImageIcon(ImageIO.read(new File("src/main/resources/images/user.png")))
            );
            setIconImage(ImageIO.read(new File("src/main/resources/images/favicon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                login();
            }
        });
    }

    private void login() {
        String userName = txtEmail.getText();
        String password = String.valueOf(txtPassword.getPassword());

        if (userName.isEmpty()) {
            Alert.showError("Authentication Error", "User Name is required!");
            return;
        }

        if (userName.isEmpty()) {
            Alert.showError("Authentication Error", "Password is required!");
            return;
        }

        password = getMD5(password);

        try {
            dataRepository.where("email", userName);
            dataRepository.where("password", password);
            List<SqlDataModel> users = dataRepository.get(UserModel.class);

            if (users.isEmpty()) {
                Alert.showError("Authentication Error", "Invalid login details!");
                txtPassword.setText(null);
                return;
            }

            setVisible(false);
            new Dashboard(dataRepository, (UserModel) users.get(0));

        } catch (DataRepositoryException e) {

            Alert.showError("Database Error", e.getMessage());

        }

    }

    private String getMD5(String str) {
        MessageDigest md = null;
        String hashText = null;
        try {
            md = MessageDigest.getInstance("MD5");
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(str.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

        } catch (NoSuchAlgorithmException e) {
            Alert.showError("System Error", e.getMessage());
        }

        return hashText;

    }
}
