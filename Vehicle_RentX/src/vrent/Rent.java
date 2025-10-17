package vrent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL; // Needed for reliable resource loading

public class Rent {
    JFrame frame;
    JLabel mainHeading;
    JButton mainButton;

    public Rent() {
        frame = new JFrame("V-RentX");
        frame.setSize(900, 650);
        frame.setLocation(450, 150);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.WHITE);
        
        
        URL iconURL = getClass().getResource("src/medias/logo.png"); 
        
        if (iconURL != null) {
            ImageIcon logoIcon = new ImageIcon(iconURL);
            
            Image img = logoIcon.getImage();
            Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImg);
            
            JLabel logoLabel = new JLabel(logoIcon);

            int logoWidth = logoIcon.getIconWidth();
            int frameWidth = frame.getWidth();
            logoLabel.setBounds((frameWidth - logoWidth) / 2, 40, logoWidth, logoIcon.getIconHeight());
            
            frame.add(logoLabel);
            
            mainHeading = new JLabel("V-RentX");
            mainHeading.setFont(new Font("Arial", Font.BOLD, 48));
            mainHeading.setHorizontalAlignment(SwingConstants.CENTER);
            mainHeading.setBounds(0, 200, 900, 80); 
            frame.add(mainHeading);
            
        } else {
            System.err.println("Logo image not found at the specified path!");
            mainHeading = new JLabel("V-RentX");
            mainHeading.setFont(new Font("Arial", Font.BOLD, 48));
            mainHeading.setHorizontalAlignment(SwingConstants.CENTER);
            mainHeading.setBounds(0, 40, 900, 80);
            frame.add(mainHeading);
        }

        JLabel subtitle = new JLabel("Vehicle Rental Management");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setBounds(0, 280, 900, 30);
        frame.add(subtitle);

        mainButton = new JButton("Continue");
        mainButton.setFont(new Font("Arial", Font.BOLD, 20));
        mainButton.setBounds(300, 400, 300, 75);
        mainButton.setFocusPainted(false);
        frame.add(mainButton);

        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginFrame();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        
        SwingUtilities.invokeLater(() -> new Rent());
    }
}
