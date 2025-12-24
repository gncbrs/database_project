package models;

public class CarImage {

    private int imageId;
    private String imagePath;

    public CarImage() {}

    public CarImage(int imageId, String imagePath) {
        this.imageId = imageId;
        this.imagePath = imagePath;
    }

    public int getImageId() { return imageId; }
    public void setImageId(int imageId) { this.imageId = imageId; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
