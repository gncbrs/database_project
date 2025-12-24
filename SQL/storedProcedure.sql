DELIMITER $$

CREATE PROCEDURE GetFullCarDetails(IN p_car_id INT)
BEGIN
    
    SELECT 
        c.car_id,
        c.brand,
        c.model,
        c.price,
        c.horse_power,
        c.year,
        c.torque
    FROM Car c
    WHERE c.car_id = p_car_id;

    SELECT 
        cs.status_name
    FROM Car_Has_Status chs
    INNER JOIN Car_Status cs ON cs.status_id = chs.status_id
    WHERE chs.car_id = p_car_id;

    SELECT 
        ci.image_path
    FROM Car_Has_Image chi
    INNER JOIN Car_Image ci ON ci.image_id = chi.image_id
    WHERE chi.car_id = p_car_id;

    SELECT 
        co.option_name,
        co.option_value
    FROM Car_Has_Option cho
    INNER JOIN Car_Option co ON co.option_id = cho.option_id
    WHERE cho.car_id = p_car_id;
END $$

DELIMITER ;
