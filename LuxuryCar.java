package java_project;

import java.time.LocalDate;

public class LuxuryCar extends Vehicle implements Bookable {

    public LuxuryCar(String registrationNumber, String brand, String model, int year, int seats) {
        super(registrationNumber, brand, model, year, seats);

        if (seats < 4 || seats > 5) {
            throw new IllegalArgumentException("Το LuxuryCar πρέπει να έχει 4-5 θέσεις.");
        }
    }

    //Override
    public double getDailyRate() {
        return 120;
    }

    //Override
    public String getCategory() {
        return "LuxuryCar";
    }

    //Override
    //exo tin method auti afou exo to implements Bookable
    public boolean isAvailable(LocalDate start, int days) {
        return true;
    }
}