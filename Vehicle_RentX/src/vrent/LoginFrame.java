package vrent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame {
    private JFrame frame;
    private JTextField tfUser;
    private JPasswordField pfPass;
    private JButton btnLogin;
    private JCheckBox showPass;

    public LoginFrame() {
        frame = new JFrame("V-RentX - Login");
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(25, 25, 112)); 
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(null);

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(new Color(255, 255, 255, 230));
        cardPanel.setBounds(100, 50, 300, 220);
        cardPanel.setLayout(null);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitle = new JLabel("Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setBounds(100, 10, 100, 30);
        cardPanel.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(30, 60, 80, 25);
        cardPanel.add(lblUser);

        tfUser = new JTextField();
        tfUser.setBounds(120, 60, 150, 25);
        tfUser.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        cardPanel.add(tfUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(30, 100, 80, 25);
        cardPanel.add(lblPass);

        pfPass = new JPasswordField();
        pfPass.setBounds(120, 100, 150, 25);
        pfPass.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        cardPanel.add(pfPass);

        showPass = new JCheckBox("Show");
        showPass.setBounds(120, 130, 80, 20);
        showPass.setBackground(new Color(255, 255, 255, 0));
        cardPanel.add(showPass);

        showPass.addActionListener(e -> {
            if (showPass.isSelected()) {
                pfPass.setEchoChar((char) 0);
            } else {
                pfPass.setEchoChar('•');
            }
        });

        btnLogin = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(30, 144, 255) : new Color(70, 130, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setBounds(90, 160, 120, 35);
        cardPanel.add(btnLogin);

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

        backgroundPanel.add(cardPanel);
        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
