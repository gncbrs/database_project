package models;

public class SellerPhone {

    private int sellerId;
    private String phone;

    public SellerPhone() {}

    public SellerPhone(int sellerId, String phone) {
        this.sellerId = sellerId;
        this.phone = phone;
    }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
