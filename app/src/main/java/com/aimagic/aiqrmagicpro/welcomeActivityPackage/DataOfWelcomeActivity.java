package com.aimagic.aiqrmagicpro.welcomeActivityPackage;

public class DataOfWelcomeActivity {

    private int imageOfWelcome;
    private String descriptionOfWelcome;

    public DataOfWelcomeActivity(int imageOfWelcome, String descriptionOfWelcome) {
        this.imageOfWelcome = imageOfWelcome;
        this.descriptionOfWelcome = descriptionOfWelcome;
    }

    public int getImageOfWelcome() {
        return imageOfWelcome;
    }

    public void setImageOfWelcome(int imageOfWelcome) {
        this.imageOfWelcome = imageOfWelcome;
    }

    public String getDescriptionOfWelcome() {
        return descriptionOfWelcome;
    }

    public void setDescriptionOfWelcome(String descriptionOfWelcome) {
        this.descriptionOfWelcome = descriptionOfWelcome;
    }

}
