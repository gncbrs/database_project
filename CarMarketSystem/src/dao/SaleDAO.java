package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class SaleDAO {

    // Yeni satış ekle
    public static boolean addSale(int customerId, int sellerId, int carId, double price) throws SQLException {
        String query = "INSERT INTO Sale (customer_id, seller_id, car_id, sale_date, price) "
                + "VALUES (?, ?, ?, NOW(), ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ps.setInt(2, sellerId);
            ps.setInt(3, carId);
            ps.setDouble(4, price);

            return ps.executeUpdate() > 0;
        }
    }

    // Add sale without requiring car to exist (car is deleted before sale is
    // recorded)
    public static boolean addSaleWithoutCarFK(int customerId, int sellerId, int carId, double price) {
        // Store sale with NULL car_id since the car is already deleted
        String query = "INSERT INTO Sale (customer_id, seller_id, car_id, sale_date, price) "
                + "VALUES (?, ?, NULL, NOW(), ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ps.setInt(2, sellerId);
            ps.setDouble(3, price);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("addSaleWithoutCarFK ERROR: " + e.getMessage());
            return false;
        }
    }

    // Tüm satışları listele
    public static ArrayList<String> getAllSales() throws SQLException {
        ArrayList<String> list = new ArrayList<>();

        String query = "SELECT s.sale_id, c.first_name AS customer, se.first_name AS seller, " +
                "car.brand, car.model, s.price, s.sale_date " +
                "FROM Sale s " +
                "INNER JOIN Customer c ON c.customer_id = s.customer_id " +
                "INNER JOIN Seller se ON se.seller_id = s.seller_id " +
                "INNER JOIN Car car ON car.car_id = s.car_id";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String info = "Sale #" + rs.getInt("sale_id") +
                        " | Customer: " + rs.getString("customer") +
                        " | Seller: " + rs.getString("seller") +
                        " | Car: " + rs.getString("brand") + " " + rs.getString("model") +
                        " | Price: " + rs.getDouble("price") +
                        " | Date: " + rs.getTimestamp("sale_date");

                list.add(info);
            }

        }

        return list;
    }

    // Bir müşterinin satın aldığı arabalar
    public static ArrayList<String> getSalesByCustomer(int customerId) {
        ArrayList<String> list = new ArrayList<>();

        String query = "SELECT car.brand, car.model, s.price, s.sale_date " +
                "FROM Sale s " +
                "INNER JOIN Car car ON car.car_id = s.car_id " +
                "WHERE s.customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String info = rs.getString("brand") + " " +
                        rs.getString("model") +
                        " | Price: " + rs.getDouble("price") +
                        " | Date: " + rs.getTimestamp("sale_date");

                list.add(info);
            }

        } catch (SQLException e) {
            System.out.println("getSalesByCustomer ERROR: " + e.getMessage());
        }

        return list;
    }

    // Bir satıcının yaptığı satışlar
    public static ArrayList<String> getSalesBySeller(int sellerId) {
        ArrayList<String> list = new ArrayList<>();

        String query = "SELECT car.brand, car.model, s.price, s.sale_date " +
                "FROM Sale s " +
                "INNER JOIN Car car ON car.car_id = s.car_id " +
                "WHERE s.seller_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String info = rs.getString("brand") + " " +
                        rs.getString("model") +
                        " | Price: " + rs.getDouble("price") +
                        " | Date: " + rs.getTimestamp("sale_date");

                list.add(info);
            }

        } catch (SQLException e) {
            System.out.println("getSalesBySeller ERROR: " + e.getMessage());
        }

        return list;
    }
}
