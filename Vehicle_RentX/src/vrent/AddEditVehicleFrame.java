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
    private JButton btnSubmit;
    private Vehicle editing;
    private DashboardFrame parent;

    public AddEditVehicleFrame(Vehicle vehicleToEdit, DashboardFrame parent) {
        this.editing = vehicleToEdit;
        this.parent = parent;
        frame = new JFrame(editing == null ? "Add Vehicle" : "Edit Vehicle");
        frame.setSize(450, 350);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 30, 100, 25);
        frame.add(lblName);
        tfName = new JTextField();
        tfName.setBounds(150, 30, 250, 25);
        frame.add(tfName);

        JLabel lblType = new JLabel("Type:");
        lblType.setBounds(30, 70, 100, 25);
        frame.add(lblType);
        tfType = new JTextField();
        tfType.setBounds(150, 70, 250, 25);
        frame.add(tfType);

        JLabel lblAvailable = new JLabel("Available:");
        lblAvailable.setBounds(30, 110, 100, 25);
        frame.add(lblAvailable);
        cbAvailable = new JCheckBox();
        cbAvailable.setBounds(150, 110, 20, 25);
        frame.add(cbAvailable);

        JLabel lblCustomer = new JLabel("Customer:");
        lblCustomer.setBounds(30, 150, 100, 25);
        frame.add(lblCustomer);
        tfCustomer = new JTextField();
        tfCustomer.setBounds(150, 150, 250, 25);
        frame.add(tfCustomer);

        btnSubmit = new JButton(editing == null ? "Add" : "Save");
        btnSubmit.setBounds(150, 210, 120, 35);
        frame.add(btnSubmit);

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
                        JOptionPane.showMessageDialog(frame, "Vehicle added");
                        frame.dispose();
                        parent.refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Insert failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    editing.setName(name);
                    editing.setType(type);
                    editing.setAvailable(available);
                    editing.setCustomerName(customer);
                    boolean ok = DBHelper.updateVehicle(editing);
                    if (ok) {
                        JOptionPane.showMessageDialog(frame, "Updated");
                        frame.dispose();
                        parent.refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.setVisible(true);
    }
}