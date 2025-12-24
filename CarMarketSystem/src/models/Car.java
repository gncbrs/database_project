package models;

public class Car {

    private int carId;
    private String brand;
    private String model;
    private double price;
    private int horsePower;
    private int year;
    private int torque;

    public Car() {}

    public Car(int carId, String brand, String model, double price, int horsePower, int year, int torque) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.horsePower = horsePower;
        this.year = year;
        this.torque = torque;
    }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getHorsePower() { return horsePower; }
    public void setHorsePower(int horsePower) { this.horsePower = horsePower; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getTorque() { return torque; }
    public void setTorque(int torque) { this.torque = torque; }
}
