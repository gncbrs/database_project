package dao;

import database.DatabaseConnection;
import models.CarImage;
import java.sql.*;
import java.util.ArrayList;

public class CarImageDAO {

    // Bir arabanın tüm resimlerini getir (Car_Has_Image üzerinden)
    public static ArrayList<CarImage> getImagesByCarId(int carId) {
        ArrayList<CarImage> list = new ArrayList<>();

        String query = 
            "SELECT ci.image_id, ci.image_path " +
            "FROM Car_Image ci " +
            "INNER JOIN Car_Has_Image chi ON ci.image_id = chi.image_id " +
            "WHERE chi.car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CarImage img = new CarImage(
                    rs.getInt("image_id"),
                    rs.getString("image_path")
                );
                list.add(img);
            }

        } catch (SQLException e) {
            System.out.println("getImagesByCarId ERROR: " + e.getMessage());
        }

        return list;
    }


    // Yeni resim ekle
    public static int addImage(String path) {
        int generatedId = -1;

        String query = "INSERT INTO Car_Image (image_path) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, path);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1); // Eklenen resmin ID'si
            }

        } catch (SQLException e) {
            System.out.println("addImage ERROR: " + e.getMessage());
        }

        return generatedId;
    }


    // Araba–Resim eşlemesi ekle
    public static boolean attachImageToCar(int carId, int imageId) {
        String query = "INSERT INTO Car_Has_Image (car_id, image_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, carId);
            ps.setInt(2, imageId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("attachImageToCar ERROR: " + e.getMessage());
            return false;
        }
    }
}
