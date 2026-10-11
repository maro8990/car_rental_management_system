package java_project;

import java.time.LocalDate;
import java.util.ArrayList;

public class Rental {
    private String rentalId;
    private Customer customer;//instance variable
    private Vehicle vehicle;//instance variable 
    private LocalDate startDate;
    private int durationDays;
    private LocalDate endDate;
    private ArrayList<AdditionalService> services;

    private double baseCost;
    private double servicesCost;
    private double discount;
    private double totalCost;

    public Rental(String rentalId, Customer customer, Vehicle vehicle,
                  LocalDate startDate, int durationDays,
                  ArrayList<AdditionalService> services) {
        this.rentalId = rentalId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.durationDays = durationDays;
        this.endDate = startDate.plusDays(durationDays - 1);
        this.services = services;

        this.baseCost = calculateBaseCost();
        this.servicesCost = calculateServicesCost();
        this.discount = 0;
        this.totalCost = baseCost + servicesCost;
    }

    public double calculateBaseCost() {
        return vehicle.getDailyRate() * durationDays;
    }

    public double calculateServicesCost() {
        double total = 0;
        for (AdditionalService s : services) {
            total += s.calculateCost(durationDays);
        }
        return total;
    }

    public void applyDiscount(double discount) {
        this.discount = discount;
        this.totalCost = baseCost + servicesCost - discount;
    }

    public String getRentalId() {
        return rentalId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ArrayList<AdditionalService> getServices() {
        return services;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public double getServicesCost() {
        return servicesCost;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotalCost() {
        return totalCost;
    }
}