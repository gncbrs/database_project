package dao;

import database.DatabaseConnection;
import models.CarStatus;
import java.sql.*;
import java.util.ArrayList;

public class CarStatusDAO {

    // Tüm statüleri getir
    public static ArrayList<CarStatus> getAllStatuses() {
        ArrayList<CarStatus> list = new ArrayList<>();

        String query = "SELECT * FROM Car_Status";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                CarStatus status = new CarStatus(
                    rs.getInt("status_id"),
                    rs.getString("status_name")
                );
                list.add(status);
            }

        } catch (SQLException e) {
            System.out.println("getAllStatuses ERROR: " + e.getMessage());
        }

        return list;
    }


    // Bir arabanın statülerini getir (Car_Has_Status üzerinden)
    public static ArrayList<CarStatus> getStatusesByCarId(int carId) {
        ArrayList<CarStatus> list = new ArrayList<>();

        String query =
            "SELECT cs.status_id, cs.status_name " +
            "FROM Car_Status cs " +
            "INNER JOIN Car_Has_Status chs ON cs.status_id = chs.status_id " +
            "WHERE chs.car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CarStatus status = new CarStatus(
                    rs.getInt("status_id"),
                    rs.getString("status_name")
                );
                list.add(status);
            }

        } catch (SQLException e) {
            System.out.println("getStatusesByCarId ERROR: " + e.getMessage());
        }

        return list;
    }


    // Yeni status ekle
    public static boolean addStatus(String statusName) {
        String query = "INSERT INTO Car_Status (status_name) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, statusName);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("addStatus ERROR: " + e.getMessage());
            return false;
        }
    }


    // Arabaya statü bağla
    public static boolean attachStatusToCar(int carId, int statusId) {
        String query = "INSERT INTO Car_Has_Status (car_id, status_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, carId);
            ps.setInt(2, statusId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("attachStatusToCar ERROR: " + e.getMessage());
            return false;
        }
    }
}
