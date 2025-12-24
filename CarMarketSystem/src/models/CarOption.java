package models;

public class CarOption {

    private int optionId;
    private String optionName;
    private String optionValue;

    public CarOption() {}

    public CarOption(int optionId, String optionName, String optionValue) {
        this.optionId = optionId;
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    public int getOptionId() { return optionId; }
    public void setOptionId(int optionId) { this.optionId = optionId; }

    public String getOptionName() { return optionName; }
    public void setOptionName(String optionName) { this.optionName = optionName; }

    public String getOptionValue() { return optionValue; }
    public void setOptionValue(String optionValue) { this.optionValue = optionValue; }
}
