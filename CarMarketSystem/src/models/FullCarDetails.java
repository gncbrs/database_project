package models;

import java.util.ArrayList;

public class FullCarDetails {

    private Car car;
    private ArrayList<CarStatus> statusList;
    private ArrayList<CarImage> imageList;
    private ArrayList<CarOption> optionList;

    public FullCarDetails() {
        statusList = new ArrayList<>();
        imageList = new ArrayList<>();
        optionList = new ArrayList<>();
    }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public ArrayList<CarStatus> getStatusList() { return statusList; }
    public void setStatusList(ArrayList<CarStatus> statusList) { this.statusList = statusList; }

    public ArrayList<CarImage> getImageList() { return imageList; }
    public void setImageList(ArrayList<CarImage> imageList) { this.imageList = imageList; }

    public ArrayList<CarOption> getOptionList() { return optionList; }
    public void setOptionList(ArrayList<CarOption> optionList) { this.optionList = optionList; }
}
