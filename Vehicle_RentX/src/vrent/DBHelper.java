package vrent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/vrentx_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "nabeel@098";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // Login check
    public static boolean checkLogin(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // CRUD for vehicles
    public static List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT id, name, type, is_available, customer_name FROM vehicles ORDER BY id";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vehicle v = new Vehicle(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getBoolean("is_available"),
                        rs.getString("customer_name")
                );
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insertVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicles (name, type, is_available, customer_name) VALUES (?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getName());
            ps.setString(2, v.getType());
            ps.setBoolean(3, v.isAvailable());
            ps.setString(4, v.getCustomerName());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateVehicle(Vehicle v) {
        String sql = "UPDATE vehicles SET name = ?, type = ?, is_available = ?, customer_name = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getName());
            ps.setString(2, v.getType());
            ps.setBoolean(3, v.isAvailable());
            ps.setString(4, v.getCustomerName());
            ps.setInt(5, v.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
