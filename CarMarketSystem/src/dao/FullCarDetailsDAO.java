package dao;

import database.DatabaseConnection;
import models.*;
import java.sql.*;
import java.util.ArrayList;

public class FullCarDetailsDAO {

    public static FullCarDetails getCarDetails(int carId) {
        FullCarDetails details = new FullCarDetails();

        try {
            Connection conn = DatabaseConnection.getConnection();
            CallableStatement stmt = conn.prepareCall("{CALL GetFullCarDetails(?)}");

            stmt.setInt(1, carId);

            boolean hasResult = stmt.execute();
            int resultIndex = 1;

            while (hasResult) {
                ResultSet rs = stmt.getResultSet();

                // ---- 1. Result: Car Info ----
                if (resultIndex == 1) {
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

                // ---- 2. Result: Status List ----
                else if (resultIndex == 2) {
                    ArrayList<CarStatus> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new CarStatus(
                                rs.getInt("status_id") == 0 ? 0 : rs.getInt("status_id"),
                                rs.getString("status_name")
                        ));
                    }
                    details.setStatusList(list);
                }

                // ---- 3. Result: Image List ----
                else if (resultIndex == 3) {
                    ArrayList<CarImage> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new CarImage(
                                rs.getInt("image_id"),
                                rs.getString("image_path")
                        ));
                    }
                    details.setImageList(list);
                }

                // ---- 4. Result: Option List ----
                else if (resultIndex == 4) {
                    ArrayList<CarOption> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new CarOption(
                                rs.getInt("option_id"),
                                rs.getString("option_name"),
                                rs.getString("option_value")
                        ));
                    }
                    details.setOptionList(list);
                }

                resultIndex++;
                hasResult = stmt.getMoreResults();
            }

        } catch (SQLException e) {
            System.out.println("getCarDetails ERROR: " + e.getMessage());
        }

        return details;
    }
}
