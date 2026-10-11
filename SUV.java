package java_project;

import java.time.LocalDate;

public class SUV extends Vehicle implements Bookable {

    public SUV(String registrationNumber, String brand, String model, int year, int seats) {
        super(registrationNumber, brand, model, year, seats);

        if (seats < 5 || seats > 7) {
            throw new IllegalArgumentException("Το SUV πρέπει να έχει 5-7 θέσεις.");
        }
    }

    //Override
    public double getDailyRate() {
        return 70;
    }

    //Override
    public String getCategory() {
        return "SUV";
    }

    //Override
    //prepei na tin grapso efou exo to Bookable pou to kano implement
    public boolean isAvailable(LocalDate start, int days) {
        return true;
    }
}