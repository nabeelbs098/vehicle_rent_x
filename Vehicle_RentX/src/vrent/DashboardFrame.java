package vrent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DashboardFrame {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public DashboardFrame() {
        frame = new JFrame("V-RentX - Dashboard");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(1000, 80));

        JLabel title = new JLabel("Vehicle Inventory");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(20, 20, 250, 30);
        top.add(title);

        JTextField searchField = new JTextField();
        searchField.setBounds(300, 20, 180, 30);
        top.add(searchField);

        String[] filterOptions = {"All", "Available", "Not Available"};
        JComboBox<String> filterCombo = new JComboBox<>(filterOptions);
        filterCombo.setBounds(500, 20, 140, 30);
        top.add(filterCombo);

        JButton btnAdd = new JButton("Add Vehicle");
        btnAdd.setBounds(820, 20, 140, 35);
        top.add(btnAdd);

        frame.add(top, BorderLayout.NORTH);
        
        


        model = new DefaultTableModel(new Object[]{"No.", "Name", "Type", "Available", "Customer", "Edit", "Delete"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp, BorderLayout.CENTER);

        applyFilter("", "All");

        btnAdd.addActionListener(e -> {
            new AddEditVehicleFrame(null, DashboardFrame.this);
        });
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                applyFilter(searchField.getText(), (String) filterCombo.getSelectedItem());
            }
        });

        filterCombo.addActionListener(e -> {
            applyFilter(searchField.getText(), (String) filterCombo.getSelectedItem());
        });


        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0) {
                    int id = (int) table.getValueAt(row, 0);
                    if (col == 5) {
                        Vehicle v = new Vehicle(
                                id,
                                (String) table.getValueAt(row, 1),
                                (String) table.getValueAt(row, 2),
                                Boolean.parseBoolean(table.getValueAt(row, 3).toString()),
                                (String) table.getValueAt(row, 4)
                        );
                        new AddEditVehicleFrame(v, DashboardFrame.this);
                    } else if (col == 6) {
                        int confirm = JOptionPane.showConfirmDialog(frame, "Delete vehicle id " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            boolean ok = DBHelper.deleteVehicle(id);
                            if (ok) {
                                JOptionPane.showMessageDialog(frame, "Deleted");
                                refreshTable();
                            } else {
                                JOptionPane.showMessageDialog(frame, "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Edit"));
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Delete"));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void refreshTable() {
        List<Vehicle> list = DBHelper.getAllVehicles();
        model.setRowCount(0);
        for (Vehicle v : list) {
            model.addRow(new Object[]{
                    v.getId(),
                    v.getName(),
                    v.getType(),
                    v.isAvailable(),
                    v.getCustomerName(),
                    "Edit",
                    "Delete"
            });
        }
    }

    static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }
    
    public void applyFilter(String keyword, String availability) {
        List<Vehicle> list = DBHelper.getAllVehicles();
        model.setRowCount(0);

        for (Vehicle v : list) {
            boolean matchesKeyword = keyword.isEmpty()
                    || v.getName().toLowerCase().contains(keyword.toLowerCase())
                    || v.getType().toLowerCase().contains(keyword.toLowerCase())
                    || v.getCustomerName().toLowerCase().contains(keyword.toLowerCase());

            boolean matchesAvailability = availability.equals("All")
                    || (availability.equals("Available") && v.isAvailable())
                    || (availability.equals("Not Available") && !v.isAvailable());

            if (matchesKeyword && matchesAvailability) {
                model.addRow(new Object[]{
                    v.getId(),
                    v.getName(),
                    v.getType(),
                    v.isAvailable(),
                    v.getCustomerName(),
                    "Edit",
                    "Delete"
                });
            }
        }
    }

}