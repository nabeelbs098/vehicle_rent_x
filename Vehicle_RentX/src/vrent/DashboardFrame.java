package vrent;

import javax.swing.*;
import javax.swing.table.*;
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
        top.setBackground(new Color(52, 152, 219));

        JLabel title = new JLabel("V-RentX Vehicle Inventory");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 20, 400, 40);
        top.add(title);

        JTextField searchField = new JTextField("Search...");
        searchField.setBounds(420, 25, 180, 30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        top.add(searchField);

        String[] filterOptions = {"All", "Available", "Not Available"};
        JComboBox<String> filterCombo = new JComboBox<>(filterOptions);
        filterCombo.setBounds(610, 25, 130, 30);
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        top.add(filterCombo);

        JButton btnAdd = new JButton("➕ Add Vehicle");
        btnAdd.setBounds(770, 20, 180, 40);
        btnAdd.setBackground(Color.WHITE);
        btnAdd.setForeground(new Color(52, 152, 219));
        btnAdd.setFocusPainted(false);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdd.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true));

        btnAdd.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnAdd.setBackground(new Color(52, 152, 219));
                btnAdd.setForeground(Color.BLACK);
            }

            public void mouseExited(MouseEvent e) {
                btnAdd.setBackground(Color.BLACK);
                btnAdd.setForeground(new Color(52, 152, 219));
            }
        });

        top.add(btnAdd);
        frame.add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Id", "Name", "Type", "Availability", "Customer", "Edit", "Delete"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(189, 226, 243));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setOpaque(true);
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.BLACK);

        
        table.getColumnModel().getColumn(3).setCellRenderer(new AvailabilityRenderer());

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        frame.add(sp, BorderLayout.CENTER);

        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Edit", new Color(255, 165, 0)));    // Bright orange-yellow
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Delete", new Color(220, 20, 60)));  // Crimson red


        applyFilter("", "All");

        btnAdd.addActionListener(e -> new AddEditVehicleFrame(null, DashboardFrame.this));

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

                if (row >= 0 && (col == 5 || col == 6)) {
                    int id = row + 1; 
                    if (col == 5) {
                        Vehicle v = new Vehicle(
                                id,
                                (String) table.getValueAt(row, 1),
                                (String) table.getValueAt(row, 2),
                                "Available".equals(table.getValueAt(row, 3).toString()),
                                (String) table.getValueAt(row, 4)
                        );
                        new AddEditVehicleFrame(v, DashboardFrame.this);
                    } else {
                        int confirm = JOptionPane.showConfirmDialog(frame,
                                "Delete this vehicle?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            boolean ok = DBHelper.deleteVehicle(id);
                            if (ok) {
                                JOptionPane.showMessageDialog(frame, "Deleted Successfully!");
                                refreshTable();
                            } else {
                                JOptionPane.showMessageDialog(frame, "Delete Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void refreshTable() {
        List<Vehicle> list = DBHelper.getAllVehicles();
        model.setRowCount(0);
        int c = 1;
        for (Vehicle v : list) {
            model.addRow(new Object[]{
                    c++,
                    v.getName(),
                    v.getType(),
                    v.isAvailable() ? "Available" : "Not Available",
                    v.getCustomerName(),
                    "Edit",
                    "Delete"
            });
        }
    }

    public void applyFilter(String keyword, String availability) {
        List<Vehicle> list = DBHelper.getAllVehicles();
        model.setRowCount(0);
        int c = 1;

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
                        c++,
                        v.getName(),
                        v.getType(),
                        v.isAvailable() ? "Available" : "Not Available",
                        v.getCustomerName(),
                        "Edit",
                        "Delete"
                });
            }
        }
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text, Color bg) {
            setText(text);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setBackground(bg);
            setForeground(Color.BLACK);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    // ==== Availability Renderer (Colored Labels) ====
    static class AvailabilityRenderer extends JLabel implements TableCellRenderer {
        public AvailabilityRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            String status = value.toString();
            if (status.equalsIgnoreCase("Available")) {
                setBackground(new Color(46, 204, 113, 70));
                setForeground(new Color(39, 174, 96));
                setText("Available");
            } else {
                setBackground(new Color(231, 76, 60, 50));
                setForeground(new Color(192, 57, 43));
                setText("Not Available");
            }
            return this;
        }
    }
}
