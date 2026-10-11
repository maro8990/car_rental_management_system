package java_project;

import java.time.LocalDate;

public class EconomyCar extends Vehicle implements Bookable {

    public EconomyCar(String registrationNumber, String brand, String model, int year, int seats) {
        super(registrationNumber, brand, model, year, seats);

        if (seats < 4 || seats > 5) {
            throw new IllegalArgumentException("Το EconomyCar πρέπει να έχει 4-5 θέσεις.");
        }
    }

    //Override
    public double getDailyRate() {
        return 45;
    }

    //Override
    public String getCategory() {
        return "EconomyCar";
    }

    //Override
    public boolean isAvailable(LocalDate start, int days) {
        return true;
    }
}