package dao;

import database.DatabaseConnection;
import models.Car;
import java.sql.*;
import java.util.ArrayList;

public class ListsDAO {

    // Satıcının listelediği arabaları getir
    public static ArrayList<Car> getCarsListedBySeller(int sellerId) {
        ArrayList<Car> list = new ArrayList<>();

        String query =
            "SELECT car.car_id, car.brand, car.model, car.price, car.horse_power, car.year, car.torque " +
            "FROM Lists l " +
            "INNER JOIN Car car ON car.car_id = l.car_id " +
            "WHERE l.seller_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Car car = new Car(
                    rs.getInt("car_id"),
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getDouble("price"),
                    rs.getInt("horse_power"),
                    rs.getInt("year"),
                    rs.getInt("torque")
                );
                list.add(car);
            }

        } catch (SQLException e) {
            System.out.println("getCarsListedBySeller ERROR: " + e.getMessage());
        }

        return list;
    }



    // Yeni bir listing ekle (satıcı bir araba listeliyor)
    public static boolean addListing(int sellerId, int carId) {
        String query = "INSERT INTO Lists (seller_id, car_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, sellerId);
            ps.setInt(2, carId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("addListing ERROR: " + e.getMessage());
            return false;
        }
    }



    // Bir listing'i sil
    public static boolean removeListing(int sellerId, int carId) {
        String query = "DELETE FROM Lists WHERE seller_id = ? AND car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, sellerId);
            ps.setInt(2, carId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("removeListing ERROR: " + e.getMessage());
            return false;
        }
    }
}
