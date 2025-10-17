package vrent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame {
    private JFrame frame;
    private JTextField tfUser;
    private JPasswordField pfPass;
    private JButton btnLogin;

    public LoginFrame() {
        frame = new JFrame("V-RentX - Login");
        frame.setSize(450, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JLabel lblTitle = new JLabel("Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBounds(180, 20, 100, 30);
        frame.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 80, 100, 25);
        frame.add(lblUser);

        tfUser = new JTextField();
        tfUser.setBounds(150, 80, 220, 25);
        frame.add(tfUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 120, 100, 25);
        frame.add(lblPass);

        pfPass = new JPasswordField();
        pfPass.setBounds(150, 120, 220, 25);
        frame.add(pfPass);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 180, 120, 35);
        frame.add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfUser.getText().trim();
                String password = new String(pfPass.getPassword()).trim();
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Enter username and password", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean ok = DBHelper.checkLogin(username, password);
                if (ok) {
                    frame.dispose();
                    new DashboardFrame();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}