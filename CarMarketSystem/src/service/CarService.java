package service;

import database.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class CarService {

    public void addCarTransaction(String brand, String model, BigDecimal price, int hp, int year, int torque,
            String status, List<String> options, List<String> imagePaths) throws SQLException {

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert Car
            int carId = insertCar(conn, brand, model, price, hp, year, torque);

            // 2. Link Status
            linkStatus(conn, carId, status);

            // 3. Link Options
            linkOptions(conn, carId, options);

            // 4. Link Images
            linkImages(conn, carId, imagePaths);

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            throw e; // Re-throw to let UI know
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset default
                    // Do not close connection if it's a shared singleton, but usually we should
                    // close it.
                    // However, in this project DatabaseConnection.getConnection() returns a
                    // singleton-ish connection.
                    // If we close it, other parts might fail.
                    // Let's check DatabaseConnection implementation again.
                    // It returns a static 'conn'. Closing it will close it for everyone.
                    // For now, we won't close it to match existing pattern, but typically we
                    // should.
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }

    private int insertCar(Connection conn, String brand, String model, BigDecimal price, int hp, int year, int torque)
            throws SQLException {
        String sql = "INSERT INTO Car (brand, model, price, horse_power, year, torque) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, brand);
            ps.setString(2, model);
            ps.setBigDecimal(3, price);
            ps.setInt(4, hp);
            ps.setInt(5, year);
            ps.setInt(6, torque);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating car failed, no ID obtained.");
                }
            }
        }
    }

    private void linkStatus(Connection conn, int carId, String statusName) throws SQLException {
        // Find Status ID
        int statusId = -1;
        String findStatusSql = "SELECT status_id FROM Car_Status WHERE status_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(findStatusSql)) {
            ps.setString(1, statusName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    statusId = rs.getInt("status_id");
                } else {
                    throw new SQLException("Status not found: " + statusName);
                }
            }
        }

        // Link
        String linkSql = "INSERT INTO Car_Has_Status (car_id, status_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(linkSql)) {
            ps.setInt(1, carId);
            ps.setInt(2, statusId);
            ps.executeUpdate();
        }
    }

    private void linkOptions(Connection conn, int carId, List<String> options) throws SQLException {
        String findOptSql = "SELECT option_id FROM Car_Option WHERE option_name = ? AND option_value = ?";
        String linkOptSql = "INSERT INTO Car_Has_Option (car_id, option_id) VALUES (?, ?)";

        for (String optionStr : options) {
            String[] parts = optionStr.split(":");
            if (parts.length != 2)
                continue;

            String name = parts[0].trim();
            String value = parts[1].trim();

            int optId = -1;
            try (PreparedStatement ps = conn.prepareStatement(findOptSql)) {
                ps.setString(1, name);
                ps.setString(2, value);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        optId = rs.getInt("option_id");
                    } else {
                        // Option might not exist, skip or error? Original code assumed it exists.
                        // For safety let's skip but log
                        System.err.println("Option not found: " + optionStr);
                        continue;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(linkOptSql)) {
                ps.setInt(1, carId);
                ps.setInt(2, optId);
                ps.executeUpdate();
            }
        }
    }

    private void linkImages(Connection conn, int carId, List<String> imagePaths) throws SQLException {
        String insertImgSql = "INSERT INTO Car_Image (image_path) VALUES (?)";
        String linkImgSql = "INSERT INTO Car_Has_Image (car_id, image_id) VALUES (?, ?)";

        for (String path : imagePaths) {
            int imgId = -1;
            try (PreparedStatement ps = conn.prepareStatement(insertImgSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, path);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        imgId = rs.getInt(1);
                    }
                }
            }

            if (imgId != -1) {
                try (PreparedStatement ps = conn.prepareStatement(linkImgSql)) {
                    ps.setInt(1, carId);
                    ps.setInt(2, imgId);
                    ps.executeUpdate();
                }
            }
        }
    }
}
