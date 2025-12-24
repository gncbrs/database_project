package dao;

import database.DatabaseConnection;
import models.Car;
import java.sql.*;
import java.util.ArrayList;

public class CarDAO {

    // Tüm arabaları listele
    public static ArrayList<Car> getAllCars() throws SQLException {
        ArrayList<Car> cars = new ArrayList<>();

        String query = "SELECT * FROM Car";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("car_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("price"),
                        rs.getInt("horse_power"),
                        rs.getInt("year"),
                        rs.getInt("torque"));
                cars.add(car);
            }

        }
        return cars;
    }

    // Tek araba getir (ID ile)
    public static Car getCarById(int id) throws SQLException {
        String query = "SELECT * FROM Car WHERE car_id = ?";
        Car car = null;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                car = new Car(
                        rs.getInt("car_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("price"),
                        rs.getInt("horse_power"),
                        rs.getInt("year"),
                        rs.getInt("torque"));
            }

        }
        return car;
    }

    // Araba sil
    public static boolean deleteCar(int carId) {
        // First, delete related records from car_has_image table
        String deleteImagesQuery = "DELETE FROM car_has_image WHERE Car_car_id = ?";
        // Then, delete the car itself
        String deleteCarQuery = "DELETE FROM Car WHERE car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start transaction
            conn.setAutoCommit(false);

            try {
                // Step 1: Delete related images
                try (PreparedStatement psImages = conn.prepareStatement(deleteImagesQuery)) {
                    psImages.setInt(1, carId);
                    psImages.executeUpdate();
                }

                // Step 2: Delete the car
                try (PreparedStatement psCar = conn.prepareStatement(deleteCarQuery)) {
                    psCar.setInt(1, carId);
                    int rowsAffected = psCar.executeUpdate();

                    // Commit transaction
                    conn.commit();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                // Rollback on error
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("deleteCar ERROR: " + e.getMessage());
            return false;
        }
    }
}
