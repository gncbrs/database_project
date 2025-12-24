package dao;

import database.DatabaseConnection;
import models.CarOption;
import java.sql.*;
import java.util.ArrayList;

public class CarOptionDAO {

    // Tüm opsiyonları getir
    public static ArrayList<CarOption> getAllOptions() {
        ArrayList<CarOption> list = new ArrayList<>();

        String query = "SELECT * FROM Car_Option";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                CarOption option = new CarOption(
                    rs.getInt("option_id"),
                    rs.getString("option_name"),
                    rs.getString("option_value")
                );
                list.add(option);
            }

        } catch (SQLException e) {
            System.out.println("getAllOptions ERROR: " + e.getMessage());
        }

        return list;
    }


    // Belirli bir arabanın opsiyonlarını getir
    public static ArrayList<CarOption> getOptionsByCarId(int carId) {
        ArrayList<CarOption> list = new ArrayList<>();

        String query =
            "SELECT co.option_id, co.option_name, co.option_value " +
            "FROM Car_Option co " +
            "INNER JOIN Car_Has_Option cho ON co.option_id = cho.option_id " +
            "WHERE cho.car_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CarOption option = new CarOption(
                    rs.getInt("option_id"),
                    rs.getString("option_name"),
                    rs.getString("option_value")
                );
                list.add(option);
            }

        } catch (SQLException e) {
            System.out.println("getOptionsByCarId ERROR: " + e.getMessage());
        }

        return list;
    }


    // Yeni bir opsiyon ekle
    public static int addOption(String name, String value) {
        int generatedId = -1;

        String query = "INSERT INTO Car_Option (option_name, option_value) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, value);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("addOption ERROR: " + e.getMessage());
        }

        return generatedId;
    }


    // Arabaya opsiyon bağla
    public static boolean attachOptionToCar(int carId, int optionId) {
        String query = "INSERT INTO Car_Has_Option (car_id, option_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, carId);
            ps.setInt(2, optionId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("attachOptionToCar ERROR: " + e.getMessage());
            return false;
        }
    }
}
