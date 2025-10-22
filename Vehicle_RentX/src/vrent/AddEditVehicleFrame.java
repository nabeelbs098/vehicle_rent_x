package vrent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class AddEditVehicleFrame {
    private JFrame frame;
    private JTextField tfName, tfType, tfCustomer;
    private JCheckBox cbAvailable;
    private JButton btnSubmit, btnCancel;
    private Vehicle editing;
    private DashboardFrame parent;

    public AddEditVehicleFrame(Vehicle vehicleToEdit, DashboardFrame parent) {
        this.editing = vehicleToEdit;
        this.parent = parent;

        frame = new JFrame(editing == null ? " Add Vehicle" : " Edit Vehicle");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(236, 240, 241));

        JLabel lblTitle = new JLabel(editing == null ? "Add New Vehicle" : "Edit Vehicle");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBounds(150, 15, 300, 40);
        frame.add(lblTitle);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblName.setBounds(50, 70, 100, 25);
        frame.add(lblName);

        tfName = new JTextField();
        tfName.setBounds(180, 70, 250, 30);
        tfName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        frame.add(tfName);

        JLabel lblType = new JLabel("Type:");
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblType.setBounds(50, 120, 100, 25);
        frame.add(lblType);

        tfType = new JTextField();
        tfType.setBounds(180, 120, 250, 30);
        tfType.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        frame.add(tfType);

        JLabel lblAvailable = new JLabel("Available:");
        lblAvailable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAvailable.setBounds(50, 170, 100, 25);
        frame.add(lblAvailable);

        cbAvailable = new JCheckBox();
        cbAvailable.setBounds(180, 170, 30, 25);
        cbAvailable.setBackground(new Color(236, 240, 241));
        frame.add(cbAvailable);

        JLabel lblCustomer = new JLabel("Customer:");
        lblCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblCustomer.setBounds(50, 220, 100, 25);
        frame.add(lblCustomer);

        tfCustomer = new JTextField();
        tfCustomer.setBounds(180, 220, 250, 30);
        tfCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        frame.add(tfCustomer);

        btnSubmit = new JButton(editing == null ? "Add Vehicle" : "Save Changes");
        btnSubmit.setBounds(120, 290, 140, 40);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setBackground(new Color(46, 204, 113)); // green
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(46, 204, 113));
            }
        });

        frame.add(btnSubmit);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(280, 290, 140, 40);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(231, 76, 60)); // red
        btnCancel.setForeground(Color.BLACK);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancel.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancel.setBackground(new Color(231, 76, 60));
            }
        });

        btnCancel.addActionListener(e -> frame.dispose());
        frame.add(btnCancel);

        if (editing != null) {
            tfName.setText(editing.getName());
            tfType.setText(editing.getType());
            cbAvailable.setSelected(editing.isAvailable());
            tfCustomer.setText(editing.getCustomerName());
        }

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText().trim();
                String type = tfType.getText().trim();
                boolean available = cbAvailable.isSelected();
                String customer = tfCustomer.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Name is required", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (editing == null) {
                    Vehicle v = new Vehicle(name, type, available, customer);
                    boolean ok = DBHelper.insertVehicle(v);
                    if (ok) {
                        JOptionPane.showMessageDialog(frame, "✅ Vehicle Added!");
                        frame.dispose();
                        parent.refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Insert Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    editing.setName(name);
                    editing.setType(type);
                    editing.setAvailable(available);
                    editing.setCustomerName(customer);
                    boolean ok = DBHelper.updateVehicle(editing);
                    if (ok) {
                        JOptionPane.showMessageDialog(frame, "✅ Vehicle Updated!");
                        frame.dispose();
                        parent.refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Update Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
