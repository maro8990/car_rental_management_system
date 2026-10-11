package java_project;

public abstract class Vehicle {
	
	//encapsulation = enthilakosi --attributes pou einai private gia na xrisimopoiithoun evala getters kai setters
    private String registrationNumber;
    private String brand;
    private String model;
    private int year;
    private int seats;

    public Vehicle(String registrationNumber, String brand, String model, int year, int seats) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.seats = seats;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getSeats() {
        return seats;
    }

    public abstract double getDailyRate();//polymorphism
    public abstract String getCategory();

    //Override
    public String toString() {
        return "Vehicle [registrationNumber=" + registrationNumber +
               ", brand=" + brand +
               ", model=" + model +
               ", year=" + year +
               ", seats=" + seats + "]";
    }
}