
# Car Rental Management System

A command-line **Car Rental Management System written in Java**, developed as a university project to demonstrate object-oriented programming, file persistence, exception handling, collections, date handling, and application-level business logic.

The system manages vehicles, customers, rentals, optional services, loyalty rewards, availability checks, invoices, and rental histories through an interactive console interface.

---

## Features

- Add and remove vehicles
- Register and search customers
- Create vehicle rentals
- Check vehicle availability for specific dates
- Prevent overlapping rentals
- Search for vehicles by category and seating capacity
- Suggest alternative available vehicles
- Calculate rental costs
- Add optional rental services
- Redeem customer loyalty points
- Award loyalty points after rentals
- Generate rental invoices
- Display customer rental history
- Display vehicle rental history
- Load application data from text files
- Save updated data back to files
- Validate vehicle-specific constraints
- Handle missing customers using a custom exception

---

## Object-Oriented Design

The project uses several core object-oriented programming concepts.

### Inheritance

All vehicle types inherit from the abstract `Vehicle` class:

```text
                 Vehicle
                    |
        +-----------+-----------+
        |           |           |
   EconomyCar      SUV     Convertible
```

The base class stores common vehicle properties such as:

- Registration number
- Brand
- Model
- Manufacturing year
- Number of seats

It also defines abstract methods that subclasses must implement:

```java
public abstract double getDailyRate();
public abstract String getCategory();
```

Each vehicle type therefore provides its own category and daily rental rate.

---

## Polymorphism

The application works with vehicles through the base `Vehicle` type:

```java
for (Vehicle v : vehicles) {
    ...
}
```

At runtime, calls such as:

```java
v.getDailyRate();
v.getCategory();
```

execute the implementation belonging to the actual vehicle subtype.

For example:

```text
EconomyCar  -> €45/day
SUV         -> €70/day
Convertible -> €90/day
```

This allows different vehicle categories to be handled through a common interface.

---

## Encapsulation

Vehicle and customer information is stored in private instance variables and accessed using public methods.

Example:

```java
private String registrationNumber;
private String brand;
private String model;
private int year;
private int seats;
```

This keeps an object's internal state controlled by the class that owns it.

---

## Abstract Classes

`Vehicle` is defined as an abstract class:

```java
public abstract class Vehicle
```

It provides common data and behaviour while requiring concrete vehicle subclasses to define:

```java
getDailyRate()
getCategory()
```

---

## Interfaces

Bookable vehicles implement the `Bookable` interface:

```java
public interface Bookable {
    boolean isAvailable(LocalDate start, int days);
}
```

This demonstrates the use of Java interfaces to define behaviour that classes agree to provide.

The system-level rental availability logic is handled by `CarRentalSystem`, which checks requested dates against existing rentals.

---

## Vehicle Categories

The project supports multiple vehicle categories.

### Economy Car

```text
Daily rate: €45
Allowed seats: 4-5
```

### SUV

```text
Daily rate: €70
Allowed seats: 5-7
```

### Convertible

```text
Daily rate: €90
Allowed seats: 2-4
```

Vehicle constructors validate category-specific seating constraints and reject invalid configurations using `IllegalArgumentException`.

---

## Rental Management

A rental connects:

```text
Customer
   |
   +---- Rental ---- Vehicle
            |
            +---- Start Date
            +---- End Date
            +---- Duration
            +---- Optional Services
            +---- Discount
            +---- Total Cost
```

Each rental contains:

- Rental ID
- Customer
- Vehicle
- Start date
- Duration
- End date
- Optional services
- Base rental cost
- Service cost
- Discount
- Final cost

The end date is calculated automatically from the start date and rental duration.

---

## Rental Cost Calculation

The base rental cost is calculated as:

```text
Daily Vehicle Rate × Number of Rental Days
```

For example:

```text
SUV daily rate = €70
Rental duration = 4 days

Base cost = 70 × 4
          = €280
```

Additional services and discounts are then applied to determine the final price.

---

## Additional Services

Optional services are represented using the `AdditionalService` enum.

Supported services include:

### GPS

```text
€5 per day
```

For a 4-day rental:

```text
4 × €5 = €20
```

### Child Seat

```text
€20 fixed charge
```

The charge remains €20 regardless of rental duration.

### Extra Insurance

```text
€30 per 5-day period
```

The number of periods is rounded upward.

For example:

```text
1-5 days   -> €30
6-10 days  -> €60
11-15 days -> €90
```

Each enum value implements its own cost-calculation behaviour.

---

## Availability Checking

Before creating a rental, the system checks whether the selected vehicle is already rented during the requested period.

For a requested rental:

```text
requestedStart ---------------- requestedEnd
```

the application compares it with every existing rental of the same vehicle.

A conflict exists whenever the requested period overlaps an existing rental period.

Conceptually:

```java
if (!(requestedEnd.isBefore(rentalStart)
        || requestedStart.isAfter(rentalEnd))) {
    return false;
}
```

This prevents double-booking the same vehicle.

---

## Alternative Vehicle Suggestions

If the requested vehicle category is unavailable, the application searches for alternative vehicles that:

- Have enough seats
- Are available during the requested period

Alternatives are sorted by daily rental price:

```java
Comparator.comparingDouble(Vehicle::getDailyRate)
```

This allows the system to recommend lower-cost alternatives first.

---

## Customer Management

Each customer contains:

- Full name
- ID number
- Address
- Phone number
- Email
- Credit card number
- Loyalty account

Customers can be:

- Registered
- Looked up by ID
- Associated with rentals
- Shown together with their rental history

Duplicate customer IDs are rejected.

---

## Loyalty Program

Each customer has a `LoyaltyAccount` containing:

```text
Loyalty card number
Current points
```

Customers earn points based on the amount paid:

```java
public void addPoints(double amountPaid) {
    this.points += (int) amountPaid;
}
```

Points can also be redeemed for discounts.

A customer must redeem at least:

```text
50 points
```

The discount calculation is:

```text
10 points = €1 discount
```

For example:

```text
100 points -> €10 discount
250 points -> €25 discount
```

Redeemed points are deducted from the customer's loyalty account.

---

## Invoice Generation

After a rental is successfully created, the application can generate an invoice containing:

```text
Rental ID
Customer information
Vehicle information
Start date
End date
Base rental cost
Additional services
Service costs
Discount
Final total
```

Example:

```text
===============================
           INVOICE
===============================
Rental ID: R001

Customer:
Name: Example Customer
ID: ID001

Vehicle:
Category: SUV
Registration: ABC123

Cost:
Start Date: 2026-07-01
End Date: 2026-07-05
Base Cost: €350
GPS: €25
Discount: €10
Total: €365
===============================
```

---

## Rental History

### Customer History

The application can display all rentals associated with a particular customer.

Information includes:

```text
Rental ID
Vehicle
Rental dates
Rental cost
Current loyalty points
```

### Vehicle History

The application can also display the complete rental history of a vehicle.

This includes:

```text
Rental ID
Customer
Rental dates
Rental cost
```

---

## Custom Exception Handling

The project defines a custom checked exception:

```java
CustomerNotFoundException
```

It is used when customer information is requested for an ID that does not exist.

Example:

```java
throw new CustomerNotFoundException(
    "Customer not found: " + clientId
);
```

This demonstrates how application-specific error conditions can be represented using custom exception types.

---

## File Persistence

The application stores data using semicolon-separated text files.

### Vehicles

Vehicle records follow the format:

```text
category;registrationNumber;brand;model;year;seats
```

Example:

```text
EconomyCar;KPA123;Toyota;Yaris;2022;5
SUV;LEM345;Nissan;Qashqai;2023;5
Convertible;NIC567;Mazda;MX-5;2022;2
```

### Customers

Customer records contain:

```text
id;name;address;phone;email;creditCard;loyaltyCard;points
```

### Rentals

Rental data contains information such as:

```text
rentalId
customerId
vehicleRegistration
startDate
duration
additionalServices
discount
totalCost
```

Application data is loaded when the program starts and changes are written back to the corresponding files.

---

## Interactive Menu

The application provides the following command-line menu:

```text
Car Rental Management System

1. Vehicle Management
2. Customer Management
3. Create Rental
4. Print Invoice
5. Display Vehicle Availability
6. Display Customer History
7. Display Vehicle History
8. Print All Data
0. Exit
```

---

## Example Workflow

A typical session might involve:

```text
1. Start the application

2. Load existing vehicles, customers and rentals

3. Register a new customer

4. Search for an available SUV

5. Select the rental start date and duration

6. Add GPS and additional insurance

7. Redeem loyalty points

8. Create the rental

9. Generate the invoice

10. View the customer's rental history
```

---

## Collections Used

The application stores runtime data using Java collections:

```java
ArrayList<Vehicle> vehicles;
ArrayList<Customer> customers;
ArrayList<Rental> rentals;
```

Additional services are also stored using:

```java
ArrayList<AdditionalService>
```

Vehicle alternatives are sorted using Java's `Comparator` API.

---

## Java Concepts Demonstrated

The project demonstrates:

- Object-oriented programming
- Classes and objects
- Encapsulation
- Inheritance
- Polymorphism
- Abstract classes
- Interfaces
- Enums
- Custom exceptions
- Collections
- `ArrayList`
- `Comparator`
- File input/output
- Exception handling
- `LocalDate`
- User input with `Scanner`
- Data validation
- Object relationships
- Business logic
- Persistence to text files

---

## Project Structure

A typical repository structure is:

```text
.
├── src/
│   └── java_project/
│       ├── Main.java
│       ├── CarRentalSystem.java
│       ├── Vehicle.java
│       ├── EconomyCar.java
│       ├── SUV.java
│       ├── Convertible.java
│       ├── LuxuryCar.java
│       ├── Bookable.java
│       ├── Rental.java
│       ├── Customer.java
│       ├── LoyaltyAccount.java
│       ├── AdditionalService.java
│       └── CustomerNotFoundException.java
├── vehicles.txt
├── customer.txt
├── rentals.txt
└── README.md
```

---

## Building the Project

### Requirements

- Java Development Kit (JDK)
- Java 8 or later

The project uses only standard Java libraries.

### Compile

From the project root:

```bash
javac -d out src/java_project/*.java
```

### Run

```bash
java -cp out java_project.Main
```

The data files should be available in the application's working directory when the program starts.

---

## Design Highlights

Some of the main design decisions demonstrated by this project include:

### Common Vehicle Abstraction

Shared vehicle properties are defined once in the abstract `Vehicle` class rather than duplicated across each vehicle category.

### Runtime Polymorphism

The system can work with different vehicle subclasses through `Vehicle` references.

### Behavioural Enum

`AdditionalService` does more than store constants: each service implements its own pricing behaviour.

### Separate Domain Objects

Customers, vehicles, rentals and loyalty accounts are represented by separate classes with different responsibilities.

### Date-Based Booking Validation

Rental availability is determined by comparing requested rental periods with existing bookings.

### Persistence

Changes to vehicles, customers and rentals are saved so that application state can survive between executions.

---

## Potential Improvements

Possible future improvements include:

- Replace text-file persistence with a relational database
- Add unit tests with JUnit
- Add Maven or Gradle
- Separate persistence logic from business logic
- Add stronger validation for customer information
- Improve exception handling for malformed data files
- Add search and filtering functionality
- Add rental cancellation
- Add vehicle return processing
- Add late-return fees
- Add administrative and customer roles
- Replace the command-line interface with a graphical or web interface
- Add logging
- Add automated CI testing with GitHub Actions
- Introduce repository/service layers
- Add unique identifier generation independent of list size

---

## About

The **Car Rental Management System** is a Java university project designed to apply object-oriented programming concepts to a practical management application.

It combines inheritance, abstraction, polymorphism, interfaces, enums, custom exceptions, collections, file handling, date-based availability checking, loyalty rewards, cost calculation and persistent application data in a single interactive system.