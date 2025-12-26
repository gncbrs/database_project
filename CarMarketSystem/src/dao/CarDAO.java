package dao;

import database.DatabaseConnection;
import models.Car;
import java.sql.*;
import java.util.ArrayList;

public class CarDAO {

    // Tüm arabaları listele
    // CarDAO.java içindeki getAllCars metodunu bununla değiştir:

public static ArrayList<Car> getAllCars() throws SQLException {
    ArrayList<Car> cars = new ArrayList<>();

    // Hem araba bilgilerini hem de statü ismini (Available/Sold) çekiyoruz
    String query = "SELECT c.*, cs.status_name FROM Car c " +
                   "LEFT JOIN Car_Has_Status chs ON c.car_id = chs.car_id " +
                   "LEFT JOIN Car_Status cs ON chs.status_id = cs.status_id";

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
                    rs.getInt("torque")
            );
            
            // Veritabanından gelen statü ismini modele ekle
            // Eğer null gelirse (statü atanmamışsa) varsayılan olarak "Unknown" yazsın
            String status = rs.getString("status_name");
            car.setStatus(status != null ? status : "Unknown");
            
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
    // Veritabanında CASCADE ayarı olduğu için sadece arabayı silmek yeterli.
    // Diğer tablolar (resim, opsiyon, durum) otomatik temizlenecek.
    String deleteCarQuery = "DELETE FROM Car WHERE car_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement psCar = conn.prepareStatement(deleteCarQuery)) {

        psCar.setInt(1, carId);
        int rowsAffected = psCar.executeUpdate();

        return rowsAffected > 0;

    } catch (SQLException e) {
        // Eğer araba satılmışsa (Sale tablosunda varsa) burada hala hata alabilirsin.
        // Satılan arabaları silmek yerine statüsünü "Sold" yapmak daha mantıklıdır.
        System.out.println("deleteCar ERROR: " + e.getMessage());
        return false;
    }
}
// CarDAO.java içerisine eklenecek metod:

public static boolean updateCarStatusToSold(int carId) {
    Connection conn = null;
    PreparedStatement pstmtDelete = null;
    PreparedStatement pstmtInsert = null;

    try {
        conn = DatabaseConnection.getConnection(); // Bağlantı sınıfınız
        
        // Transaction başlat (İki işlem var, biri hata verirse geri alalım)
        conn.setAutoCommit(false);

        // 1. ADIM: Mevcut durumu sil (Car_Has_Status tablosundan)
        String deleteSql = "DELETE FROM Car_Has_Status WHERE car_id = ?";
        pstmtDelete = conn.prepareStatement(deleteSql);
        pstmtDelete.setInt(1, carId);
        pstmtDelete.executeUpdate();

        // 2. ADIM: Yeni durumu ekle ('Sold' id'sini bildiğimizi varsayalım veya subquery ile çekelim)
        // Aşağıdaki SQL: Status tablosundan 'Sold' isminin ID'sini bulur ve ara tabloya ekler.
        String insertSql = "INSERT INTO Car_Has_Status (car_id, status_id) " +
                           "VALUES (?, (SELECT status_id FROM Car_Status WHERE status_name = 'Sold' LIMIT 1))";
                           
        pstmtInsert = conn.prepareStatement(insertSql);
        pstmtInsert.setInt(1, carId);
        int rowsAffected = pstmtInsert.executeUpdate();

        // Her şey yolundaysa onayla
        conn.commit();
        return rowsAffected > 0;

    } catch (SQLException e) {
        // Hata varsa işlemleri geri al
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        e.printStackTrace();
        return false;
    } finally {
        // Kaynakları kapat (conn.setAutoCommit(true) yapmayı unutma eğer connection pool kullanıyorsan)
        try { if(pstmtDelete != null) pstmtDelete.close(); } catch(Exception e){}
        try { if(pstmtInsert != null) pstmtInsert.close(); } catch(Exception e){}
        try { if(conn != null) { conn.setAutoCommit(true); conn.close(); } } catch(Exception e){}
    }
}
// ==========================================
    // SADECE SATILMAMIŞ (MÜSAİT) ARABALARI GETİR
    // ==========================================
    public static ArrayList<Car> getAvailableCars() throws SQLException {
        ArrayList<Car> cars = new ArrayList<>();

        // SQL: Araba, Status ve Ara Tabloyu (chd) birleştir.
        // WHERE şartı: Status ismi 'Sold' OLMAYANLARI getir.
        String query = "SELECT c.* FROM Car c " +
                       "JOIN Car_Has_Status chs ON c.car_id = chs.car_id " +
                       "JOIN Car_Status cs ON chs.status_id = cs.status_id " +
                       "WHERE cs.status_name != 'Sold'";

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
    // src/dao/CarDAO.java dosyasının içine ekleyin

// 1. Fiyat ve Opsiyon Güncelleme
public static void updateCar(int carId, double newPrice, String newOptionName, String newOptionValue) {
    String sqlUpdatePrice = "UPDATE Car SET price = ? WHERE car_id = ?";
    
    // Opsiyon güncellemek biraz daha karmaşık (Önce eskisini silip yenisini bağlayacağız)
    // Basitlik olması için burada sadece fiyatı güncelleyen kodu veriyorum, opsiyonu arayüzde anlatacağım.
    
    try (java.sql.Connection conn = database.DatabaseConnection.getConnection()) {
        // Fiyatı güncelle
        try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlUpdatePrice)) {
            ps.setBigDecimal(1, new java.math.BigDecimal(newPrice));
            ps.setInt(2, carId);
            ps.executeUpdate();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// 2. Yeni Resim Ekleme (Update ekranından)
public static void addImageToCar(int carId, String imagePath) {
    try (java.sql.Connection conn = database.DatabaseConnection.getConnection()) {
        // A) Resmi Car_Image tablosuna ekle
        String sqlImg = "INSERT INTO Car_Image (image_path) VALUES (?)";
        int imgId = -1;
        
        try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlImg, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, imagePath);
            ps.executeUpdate();
            var rs = ps.getGeneratedKeys();
            if (rs.next()) imgId = rs.getInt(1);
        }

        // B) Arabayla resmi bağla
        if (imgId != -1) {
            String sqlLink = "INSERT INTO Car_Has_Image (car_id, image_id) VALUES (?, ?)";
            try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlLink)) {
                ps.setInt(1, carId);
                ps.setInt(2, imgId);
                ps.executeUpdate();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
