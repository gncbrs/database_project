package models;

public class CustomerPhone {

    private int customerId;
    private String phone;

    public CustomerPhone() {}

    public CustomerPhone(int customerId, String phone) {
        this.customerId = customerId;
        this.phone = phone;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
