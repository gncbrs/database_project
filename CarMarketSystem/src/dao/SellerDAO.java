package dao;

import database.DatabaseConnection;
import models.Seller;
import java.sql.*;
import java.util.ArrayList;

public class SellerDAO {

    // Tüm satıcıları listele
    public static ArrayList<Seller> getAllSellers() throws SQLException {
        ArrayList<Seller> list = new ArrayList<>();

        String query = "SELECT * FROM Seller";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Seller s = new Seller(
                        rs.getInt("seller_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getString("email"));
                list.add(s);
            }

        }
        return list;
    }

    // ID'ye göre satıcı getir
    public static Seller getSellerById(int id) {
        String query = "SELECT * FROM Seller WHERE seller_id = ?";
        Seller s = null;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                s = new Seller(
                        rs.getInt("seller_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name"),
                        rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println("getSellerById ERROR: " + e.getMessage());
        }

        return s;
    }

    // Yeni satıcı ekle
    public static boolean addSeller(Seller seller) {
        String query = "INSERT INTO Seller (first_name, middle_name, last_name, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, seller.getFirstName());
            ps.setString(2, seller.getMiddleName());
            ps.setString(3, seller.getLastName());
            ps.setString(4, seller.getEmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("addSeller ERROR: " + e.getMessage());
            return false;
        }
    }

    // Satıcı telefonlarını getir
    public static ArrayList<String> getPhones(int sellerId) {
        ArrayList<String> phones = new ArrayList<>();
        String query = "SELECT phone FROM Seller_Phone WHERE seller_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, sellerId);
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
