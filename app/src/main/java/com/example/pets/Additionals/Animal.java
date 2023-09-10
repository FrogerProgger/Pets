package com.example.pets.Additionals;

import androidx.annotation.NonNull;

public class Animal {

    private String petType;
    private String petName;

    private int petImageId;

    private int foodCounter;

    private int energyCounter;
    private int happinessCounter;

    public Animal(@NonNull String type, int imageId) {
        petType = type;
        petImageId = imageId;
        energyCounter = 100;
        happinessCounter = 100;
        foodCounter = 100;
    }

    public void tired(int howMuch) {
        energyCounter -= howMuch;
    }

    public void sad() {
        happinessCounter--;
    }

    public void sleep() { energyCounter++; }
    public void play(String kindOfGame) {
        switch (kindOfGame){
            case "Hand":
                tired(10);
                happinessCounter += 10;
                if (happinessCounter > 100)
                {
                    happinessCounter = 100;

                }
                break;
            case "Ball":
                tired(20);
                happinessCounter += 20;
                if (happinessCounter > 100)
                {
                    happinessCounter = 100;

                }
                break;
        }
    }


    public void eat(String kindOfFood) {
        switch (kindOfFood){
            case "Corm":
                tired(5);
                foodCounter += 10;
                if (foodCounter > 100)
                {
                    foodCounter = 100;
                }
                break;
            case "Meat":
                tired(10);
                foodCounter += 20;
                if (foodCounter > 100)
                {
                    foodCounter = 100;

                }
                break;
        }
    }

    public void hungry() {
        foodCounter--;
    }

    // Getters and Setters

    public String getPetName() {
        return petName;
    }

    public String getPetType() {
        return petType;
    }

    public int getEnergyCounter() {
        return energyCounter;
    }

    public int getHappinessCounter() {
        return happinessCounter;
    }

    public int getPetImageId() {
        return petImageId;
    }

    public int getFoodCounter() {
        return foodCounter;
    }

    public void setHappinessCounter(int happinessCounter) { this.happinessCounter = happinessCounter; }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setEnergyCounter(int energyCounter) {
        this.energyCounter = energyCounter;
    }

    public void setFoodCounter(int foodCounter) {
        this.foodCounter = foodCounter;
    }
}
