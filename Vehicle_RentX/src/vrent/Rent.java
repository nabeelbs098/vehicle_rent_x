package vrent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Rent {
    JFrame frame;
    JLabel mainHeading;
    JButton mainButton;

    public Rent() {
        frame = new JFrame("V-RentX");
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(135, 206, 250),
                        0, getHeight(), new Color(25, 25, 112)); 
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(null);

        // Load Logo
        URL iconURL = getClass().getResource("/mainproject/medias/logo.png");
        if (iconURL != null) {
            ImageIcon logoIcon = new ImageIcon(iconURL);
            Image scaledImg = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImg);

            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setBounds((900 - 150) / 2, 40, 150, 150);
            backgroundPanel.add(logoLabel);
        } else {
            System.err.println("Logo not found!");
        }

        
        mainHeading = new JLabel("V-RentX");
        mainHeading.setFont(new Font("SansSerif", Font.BOLD, 52));
        mainHeading.setForeground(Color.WHITE);
        mainHeading.setHorizontalAlignment(SwingConstants.CENTER);
        mainHeading.setBounds(0, 220, 900, 60);
        backgroundPanel.add(mainHeading);

        
        JLabel subtitle = new JLabel("Vehicle Rental Management");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 22));
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setBounds(0, 280, 900, 30);
        backgroundPanel.add(subtitle);

        mainButton = new JButton("Continue") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(30, 144, 255) : new Color(70, 130, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        mainButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainButton.setForeground(Color.WHITE);
        mainButton.setFocusPainted(false);
        mainButton.setContentAreaFilled(false);
        mainButton.setBorderPainted(false);
        mainButton.setBounds(300, 400, 300, 70);
        backgroundPanel.add(mainButton);

        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginFrame();
            }
        });

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        SwingUtilities.invokeLater(Rent::new);
    }
}
