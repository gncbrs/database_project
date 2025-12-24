CREATE TABLE Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    middle_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100)
);

CREATE TABLE Customer_Phone (
    customer_id INT,
    phone VARCHAR(20),
    PRIMARY KEY (phone),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
        ON DELETE CASCADE
);

CREATE TABLE Seller (
    seller_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    middle_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100)
);

CREATE TABLE Seller_Phone (
    seller_id INT,
    phone VARCHAR(20),
    PRIMARY KEY (phone),
    FOREIGN KEY (seller_id) REFERENCES Seller(seller_id)
        ON DELETE CASCADE
);

CREATE TABLE Car (
    car_id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(50),
    model VARCHAR(50),
    price DECIMAL(10,2),
    horse_power INT,
    year INT,
    torque INT
);

CREATE TABLE Lists (
    seller_id INT,
    car_id INT,
    PRIMARY KEY (seller_id, car_id),
    FOREIGN KEY (seller_id) REFERENCES Seller(seller_id)
        ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES Car(car_id)
        ON DELETE CASCADE
);

CREATE TABLE Sale (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    sale_date DATE,
    car_id INT,
    customer_id INT,
    seller_id INT,
    FOREIGN KEY (car_id) REFERENCES Car(car_id),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (seller_id) REFERENCES Seller(seller_id)
);

CREATE TABLE Car_Status (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50)
);

CREATE TABLE Car_Has_Status (
    car_id INT,
    status_id INT,
    PRIMARY KEY (car_id, status_id),
    FOREIGN KEY (car_id) REFERENCES Car(car_id),
    FOREIGN KEY (status_id) REFERENCES Car_Status(status_id)
);

CREATE TABLE Car_Option (
    option_id INT AUTO_INCREMENT PRIMARY KEY,
    option_name VARCHAR(50),
    option_value VARCHAR(100)
);

CREATE TABLE Car_Has_Option (
    car_id INT,
    option_id INT,
    PRIMARY KEY (car_id, option_id),
    FOREIGN KEY (car_id) REFERENCES Car(car_id),
    FOREIGN KEY (option_id) REFERENCES Car_Option(option_id)
);

CREATE TABLE Car_Image (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    image_path VARCHAR(255)
);

CREATE TABLE Car_Has_Image (
    car_id INT,
    image_id INT,
    PRIMARY KEY (car_id, image_id),
    FOREIGN KEY (car_id) REFERENCES Car(car_id),
    FOREIGN KEY (image_id) REFERENCES Car_Image(image_id)
);

INSERT INTO Customer (first_name, middle_name, last_name, email)
VALUES
('Cihat', NULL, 'Oz', 'cihatoz@gmail.com'),
('Gonenc', 'Baris', 'Bezik', 'gonencbarisbezik@gmail.com');

INSERT INTO Customer_Phone (customer_id, phone)
VALUES
(1, '5551112233'),
(1, '5554445566'),
(2, '5559998877');

INSERT INTO Seller (first_name, middle_name, last_name, email)
VALUES
('Onur', NULL, 'Kadioglu', 'onurkadioglu@gmail.com'),
('Cagla', 'Ece', 'Aydoslu', 'caglaeceaydoslu@gmail.com');

INSERT INTO Seller_Phone (seller_id, phone)
VALUES
(1, '5321114455'),
(2, '5379988776');

INSERT INTO Car (brand, model, price, horse_power, year, torque)
VALUES
('Ford', 'Focus', 450000, 120, 2025, 300),
('BMW', '535i', 1450000, 170, 2021, 250),
('Toyota', 'Corolla', 520000, 115, 2018, 230);

INSERT INTO Car_Status (status_name)
VALUES
('Available'),
('Sold'),
('Pending'),
('Reserved');

INSERT INTO Car_Has_Status (car_id, status_id)
VALUES
(1, 1),  
(2, 4),  
(3, 1);  

INSERT INTO Car_Option (option_name, option_value)
VALUES
('Color', 'White'),
('Color', 'Black'),
('Package', 'Sport'),
('Package', 'Comfort');

INSERT INTO Car_Has_Option (car_id, option_id)
VALUES
(1, 1),  
(1, 4),  
(2, 2),  
(2, 3),  
(3, 1); 

INSERT INTO Car_Image (image_path)
VALUES
('images/focus1.jpg'),
('images/focus2.jpg'),
('images/bmw1.jpg'),
('images/corolla1.jpg');

INSERT INTO Car_Has_Image (car_id, image_id)
VALUES
(1, 1),   
(1, 2),   
(2, 3),   
(3, 4);   

INSERT INTO Lists (seller_id, car_id)
VALUES
(1, 1),
(1, 3),
(2, 2);

INSERT INTO Sale (sale_date, car_id, customer_id, seller_id)
VALUES
('2024-02-10', 2, 1, 2);