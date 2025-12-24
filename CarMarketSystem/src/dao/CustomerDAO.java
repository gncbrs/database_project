package dao;

import database.DatabaseConnection;
import models.Customer;
import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {

    // Tüm müşterileri listele
    public static ArrayList<Customer> getAllCustomers() throws SQLException {
        ArrayList<Customer> list = new ArrayList<>();

        String query = "SELECT * FROM Customer";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getString("email"));
                list.add(c);
            }

        }
        return list;
    }

    // ID'ye göre müşteri getir
    public static Customer getCustomerById(int id) {
        String query = "SELECT * FROM Customer WHERE customer_id = ?";
        Customer c = null;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println("getCustomerById ERROR: " + e.getMessage());
        }

        return c;
    }

    // Müşteri ekle
    public static boolean addCustomer(Customer customer) {
        String query = "INSERT INTO Customer (first_name, middle_name, last_name, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getMiddleName());
            ps.setString(3, customer.getLastName());
            ps.setString(4, customer.getEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("addCustomer ERROR: " + e.getMessage());
            return false;
        }
    }

    // Müşteri telefonlarını getir
    public static ArrayList<String> getPhones(int customerId) {
        ArrayList<String> phones = new ArrayList<>();
        String query = "SELECT phone FROM Customer_Phone WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                phones.add(rs.getString("phone"));
            }

        } catch (SQLException e) {
            System.out.println("getPhones ERROR: " + e.getMessage());
        }
        return phones;
    }
}
