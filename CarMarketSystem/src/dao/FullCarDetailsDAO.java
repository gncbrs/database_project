package dao;

import database.DatabaseConnection;
import models.*;
import java.sql.*;
import java.util.ArrayList;

public class FullCarDetailsDAO {

    public static FullCarDetails getCarDetails(int carId) {
        FullCarDetails details = new FullCarDetails();
        
        // Listeleri null kalmasın diye en başta oluşturuyoruz
        details.setStatusList(new ArrayList<>());
        details.setImageList(new ArrayList<>());
        details.setOptionList(new ArrayList<>());

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();

            // ---------------------------------------------------
            // 1. ARABA BİLGİSİNİ ÇEK
            // ---------------------------------------------------
            String sqlCar = "SELECT * FROM Car WHERE car_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlCar)) {
                ps.setInt(1, carId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Car car = new Car(
                        rs.getInt("car_id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getDouble("price"),
                        rs.getInt("horse_power"),
                        rs.getInt("year"),
                        rs.getInt("torque")
                    );
                    details.setCar(car);
                }
            }

            // ---------------------------------------------------
            // 2. STATUS (DURUM) LİSTESİNİ ÇEK (Hata veren yer burasıydı)
            // ---------------------------------------------------
            // Artık status_id ve status_name'i elle seçtiğimiz için hata vermeyecek.
            String sqlStatus = "SELECT cs.status_id, cs.status_name " +
                               "FROM Car_Status cs " +
                               "JOIN Car_Has_Status chs ON cs.status_id = chs.status_id " +
                               "WHERE chs.car_id = ?";
            
            ArrayList<CarStatus> statusList = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlStatus)) {
                ps.setInt(1, carId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    statusList.add(new CarStatus(
                        rs.getInt("status_id"),
                        rs.getString("status_name")
                    ));
                }
            }
            details.setStatusList(statusList);

            // ---------------------------------------------------
            // 3. RESİM LİSTESİNİ ÇEK
            // ---------------------------------------------------
            String sqlImage = "SELECT ci.image_id, ci.image_path " +
                              "FROM Car_Image ci " +
                              "JOIN Car_Has_Image chi ON ci.image_id = chi.image_id " +
                              "WHERE chi.car_id = ?";
            
            ArrayList<CarImage> imageList = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlImage)) {
                ps.setInt(1, carId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    imageList.add(new CarImage(
                        rs.getInt("image_id"),
                        rs.getString("image_path")
                    ));
                }
            }
            details.setImageList(imageList);

            // ---------------------------------------------------
            // 4. OPSİYON LİSTESİNİ ÇEK
            // ---------------------------------------------------
            String sqlOption = "SELECT co.option_id, co.option_name, co.option_value " +
                               "FROM Car_Option co " +
                               "JOIN Car_Has_Option cho ON co.option_id = cho.option_id " +
                               "WHERE cho.car_id = ?";
            
            ArrayList<CarOption> optionList = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlOption)) {
                ps.setInt(1, carId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    optionList.add(new CarOption(
                        rs.getInt("option_id"),
                        rs.getString("option_name"),
                        rs.getString("option_value")
                    ));
                }
            }
            details.setOptionList(optionList);

        } catch (SQLException e) {
            System.out.println("getCarDetails ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Bağlantıyı manuel kapatalım (DatabaseConnection yapına göre değişebilir)
            try { if (conn != null && !conn.isClosed()) conn.close(); } catch (SQLException e) {}
        }

        return details;
    }
}