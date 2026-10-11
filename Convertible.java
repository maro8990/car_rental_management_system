package java_project;

import java.time.LocalDate;

public class Convertible extends Vehicle  implements Bookable {
	
	// Constructor pou arxikopoiei ta stoixeia tou oximatos
    // kai elegxei an oi theseis einai mesa sto epitrepto euros gia Convertible.

    public Convertible(String registrationNumber, String brand, String model, int year, int seats) {
        super(registrationNumber, brand, model, year, seats);

        if (seats < 2 || seats > 4) {
            throw new IllegalArgumentException("Το Convertible πρέπει να έχει 2-4 θέσεις.");
        }
    }

    public double getDailyRate() {
        return 90;
    }

    
    public String getCategory() {
        return "Convertible";
    }

    // I  diathesimotita elegxetai  mesa sto CarRentalSystem me vasi tis iparxouses enoikiaseis tou oximatos
    //auti i method iparxei afou kanei implement to Bookable
    public boolean isAvailable(LocalDate start, int days) {
        return true;
    }
}