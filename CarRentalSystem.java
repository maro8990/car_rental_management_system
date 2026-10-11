package java_project;

import java.io.File;//gia diaxirisi arxeion
import java.io.FileNotFoundException;//exception an den vrethei to arxeio
import java.io.FileOutputStream;//gia na grapso se arxeio
import java.io.PrintWriter;//gia tipoma/apothikeusi se arxeio
import java.time.LocalDate;//gia diaxirisi imerominion
import java.util.ArrayList;
import java.util.Comparator;//library me voitha se taxinomisi
import java.util.Scanner;

public class CarRentalSystem {

	//Listes pou kratoun ta dedomena stin mnimi
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Customer> customers;
    private ArrayList<Rental> rentals;
    
    //counter gia na dimiourgountai monadika rental ids
    private int rentalCounter = 1;

    public CarRentalSystem() {
    	//arxikopoiisi liston
        this.vehicles = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.rentals = new ArrayList<>();
    }

    public boolean customerExists(String clientId) {
        for (Customer customer : customers) {
            if (customer.getIdNumber().equalsIgnoreCase(clientId)) {
                return true;
            }
        }
        return false;
    }

    public boolean registrationExists(String regNumber) {
        for (Vehicle v : vehicles) {
            if (v.getRegistrationNumber().equalsIgnoreCase(regNumber)) {
                return true;
            }
        }
        return false;
    }

    public Customer findCustomerById(String clientId) {
        for (Customer c : customers) {
            if (c.getIdNumber().equalsIgnoreCase(clientId)) {
                return c;
            }
        }
        return null;
    }

    public Vehicle findVehicleByRegistration(String regNumber) {
        for (Vehicle v : vehicles) {
            if (v.getRegistrationNumber().equalsIgnoreCase(regNumber)) {
                return v;
            }
        }
        return null;
    }

    private String createRentalId() {
        String id = "R" + String.format("%03d", rentalCounter);
        rentalCounter++;
        return id;
    }

    public void loadVehiclesFromFile() {
        try {
            File file = new File("vehicles.txt");
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(";");

                String category = parts[0];
                String regNumber = parts[1];
                String brand = parts[2];
                String model = parts[3];
                int year = Integer.parseInt(parts[4]);
                int seats = Integer.parseInt(parts[5]);

                Vehicle v = null;

                if (category.equalsIgnoreCase("EconomyCar")) {
                    v = new EconomyCar(regNumber, brand, model, year, seats);
                }
                else if (category.equalsIgnoreCase("SUV")) {
                    v = new SUV(regNumber, brand, model, year, seats);
                } 
                else if (category.equalsIgnoreCase("Convertible")) {
                    v = new Convertible(regNumber, brand, model, year, seats);
                } 
                else if (category.equalsIgnoreCase("LuxuryCar")) {
                    v = new LuxuryCar(regNumber, brand, model, year, seats);
                }

                if (v != null) {
                    vehicles.add(v);
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Δεν βρέθηκε το αρχείο vehicles.txt");
        }
    }

    public void loadCustomersFromFile() {
        try {
            File file = new File("customer.txt");
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");

                if (parts.length < 8) {
                    System.out.println("Λάθος γραμμή στο customer.txt: " + line);
                    continue;
                }

                String idNumber = parts[0];
                String fullName = parts[1];
                String address = parts[2];
                String phone = parts[3];
                String email = parts[4];
                String creditCardNumber = parts[5];
                String loyaltyCardNumber = parts[6];
                int points = Integer.parseInt(parts[7]);

                // dimiourgei ton logariasmo loyalty me ton arithmo kartas kai tous pontous
                LoyaltyAccount loyaltyAccount = new LoyaltyAccount(loyaltyCardNumber, points);
                
             // dimiourgei neo antikeimeno Customer me ola ta stoixeia tou pelati
                Customer customer = new Customer(fullName, idNumber, address, phone, email, creditCardNumber, loyaltyAccount);

                customers.add(customer);
            }

            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Δεν βρέθηκε το αρχείο customer.txt");
        }
    }

    public void loadRentalsFromFile() {
        try {
            File file = new File("rentals.txt");
            Scanner reader = new Scanner(file);

            //gia elegxo an iparxei nea grammi
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(";");

                String rentalId = parts[0];
                String customerId = parts[1];
                String vehicleReg = parts[2];
                LocalDate startDate = LocalDate.parse(parts[3]);
                int durationDays = Integer.parseInt(parts[4]);

                ArrayList<AdditionalService> services = new ArrayList<>();
                if (!parts[5].equalsIgnoreCase("NONE")) {
                    String[] serviceParts = parts[5].split(",");
                    for (String s : serviceParts) {
                        services.add(AdditionalService.valueOf(s.trim().toUpperCase()));
                    }
                }
                
                //metatropi apo string se double
                double discount = Double.parseDouble(parts[6]);

                Customer customer = findCustomerById(customerId);
                Vehicle vehicle = findVehicleByRegistration(vehicleReg);

                if (customer != null && vehicle != null) {
                    Rental rental = new Rental(rentalId, customer, vehicle, startDate, durationDays, services);
                    rental.applyDiscount(discount);
                    rentals.add(rental);
                }
            }

            reader.close();

            int maxId = 0;
            //me to for loop elegxo ola ta ids kai vrisko to megalitero gia na mporo na dimiourgiso pelati me neo id
            for (Rental r : rentals) {
                try {//me to substirng pianei to kommati tou string-id apo tin thesi ena mexri to telos tou string
                    int num = Integer.parseInt(r.getRentalId().substring(1));
                    if (num > maxId) 
                    	maxId = num;
                } catch (NumberFormatException e) { }
            }
            rentalCounter = maxId + 1;

        } catch (FileNotFoundException e) {
            System.out.println("Δεν βρέθηκε το αρχείο rentals.txt");
        }
    }

    private void saveVehiclesToFile() {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("vehicles.txt"));

            for (Vehicle v : vehicles) {
                String line = v.getCategory() + ";" + v.getRegistrationNumber() + ";" +
                        v.getBrand() + ";" + v.getModel() + ";" +
                        v.getYear() + ";" + v.getSeats();
                pw.println(line);
            }

            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Σφάλμα ανοίγματος αρχείου.");
        }
    }

    private void saveCustomersToFile() {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("customer.txt"));

            for (Customer c : customers) {
                String line = c.getIdNumber() + ";" + c.getFullName() + ";" +
                        c.getAddress() + ";" + c.getPhone() + ";" +
                        c.getEmail() + ";" + c.getCreditCardNumber() + ";" +
                        c.getLoyaltyAccount().getLoyaltyCardNumber() + ";" +
                        c.getLoyaltyAccount().getPoints();
                pw.println(line);
            }

            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Σφάλμα ανοίγματος αρχείου.");
        }
    }

    private void saveRentalsToFile() {
        try {
            File file = new File("rentals.txt");
         // anoigei to arxeio gia na grapsoume mesa
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            
            for (Rental r : rentals) {
                String servicesText = "NONE";
                if (!r.getServices().isEmpty()) {
                	
                	//dimiourgo antikeimenou tipou StringBuilder opou vasika me voitha gia na dimiourgiso ena string xrisimopoiontas apped tou prostheto lexeis
                	//den xriazetai na kano import kati
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < r.getServices().size(); i++) {
                    	
                    	//to r einai to antikeimeno tis rental
                    	//getServices epistrefei ti lista me tis ipiresies (gps,child_seat) (method tou Rental)
                    	//to get(i) pernei to i-osto stixeio apo ti lista(method tou ArrayList)
                    	//kai to name pairnei to onoma tou enum san string(method tou enum)
                    	//to append ta prosthetei(method tou StringBuilder)
                        sb.append(r.getServices().get(i).name());
                        
                        
                        if (i < r.getServices().size() - 1) {
                            sb.append(",");
                        }
                    }
                    //kano toString gia na grafei to StringBuilder san kanoniko string
                    servicesText = sb.toString();
                }

                String line = r.getRentalId() + ";" +
                        r.getCustomer().getIdNumber() + ";" +
                        r.getVehicle().getRegistrationNumber() + ";" +
                        r.getStartDate() + ";" +
                        r.getDurationDays() + ";" +
                        servicesText + ";" +
                        r.getDiscount() + ";" +
                        r.getTotalCost();

                pw.println(line);
            }

            pw.close();

        } catch (FileNotFoundException e) {
            System.out.println("Σφάλμα ανοίγματος αρχείου rentals.txt");
        }
    }
    public void addVehicle(Scanner input) {
        String category;
        String regNumber;
        String brand;
        String model;
        int year;
        int seats;

        System.out.print("Δώσε κατηγορία (EconomyCar / SUV / Convertible / LuxuryCar): ");
        category = input.nextLine();

        while (true) {
            System.out.print("Δώσε αριθμό εγγραφής: ");
            regNumber = input.nextLine();

            if (registrationExists(regNumber)) {
                System.out.println("Σφάλμα: Υπάρχει ήδη όχημα με αυτόν τον αριθμό εγγραφής.");
                System.out.println("Δώσε ξανά την εισαγωγή του αριθμού εγγραφής:");
            } else {
                break;
            }
        }

        System.out.print("Δώσε μάρκα: ");
        brand = input.nextLine();

        System.out.print("Δώσε μοντέλο: ");
        model = input.nextLine();

        System.out.print("Δώσε έτος κατασκευής: ");
        year = input.nextInt();

        System.out.print("Δώσε αριθμό θέσεων: ");
        seats = input.nextInt();
        input.nextLine();

        Vehicle v = null;

        try {
            if (category.equalsIgnoreCase("EconomyCar")) {
                v = new EconomyCar(regNumber, brand, model, year, seats);
            } else if (category.equalsIgnoreCase("SUV")) {
                v = new SUV(regNumber, brand, model, year, seats);
            } else if (category.equalsIgnoreCase("Convertible")) {
                v = new Convertible(regNumber, brand, model, year, seats);
            } else if (category.equalsIgnoreCase("LuxuryCar")) {
                v = new LuxuryCar(regNumber, brand, model, year, seats);
            } else {
                System.out.println("Μη έγκυρη κατηγορία οχήματος.");
                return;
            }

            vehicles.add(v);
            saveVehiclesToFile();
            System.out.println("Το όχημα προστέθηκε επιτυχώς!");

        } catch (IllegalArgumentException e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    public void removeVehicle(Scanner input) {
        System.out.print("Δώσε αριθμό εγγραφής του οχήματος που θέλεις να διαγράψεις: ");
        String regNumber = input.nextLine();

        Vehicle vehicleToRemove = null;

        for (Vehicle v : vehicles) {
            if (v.getRegistrationNumber().equalsIgnoreCase(regNumber)) {
                vehicleToRemove = v;
                break;
            }
        }

        if (vehicleToRemove != null) {
            vehicles.remove(vehicleToRemove);
            saveVehiclesToFile();
            System.out.println("Το όχημα διαγράφηκε επιτυχώς!");
        } else {
            System.out.println("Δεν βρέθηκε όχημα με αυτόν τον αριθμό εγγραφής.");
        }
    }

    public void registerCustomer(Scanner input, String client_id) {
        System.out.print("Όνομα και επώνυμο πελάτη: ");
        String fullName = input.nextLine();

        System.out.print("Διεύθυνση: ");
        String address = input.nextLine();

        System.out.print("Τηλέφωνο: ");
        String phone = input.nextLine();

        System.out.print("Email: ");
        String email = input.nextLine();

        System.out.print("Aριθμός πιστωτικής κάρτας: ");
        String creditCardNumber = input.nextLine();

        String loyaltyCardNumber = "L" + String.format("%03d", customers.size() + 1);
        LoyaltyAccount loyaltyAccount = new LoyaltyAccount(loyaltyCardNumber, 0);

        Customer customer = new Customer(fullName, client_id, address, phone, email, creditCardNumber, loyaltyAccount);

        customers.add(customer);
        saveCustomersToFile();

        System.out.println("Ο πελάτης καταχωρήθηκε επιτυχώς!");
    }

    public boolean isVehicleAvailable(Vehicle vehicle, LocalDate requestedStart, int requestedDays) {
        LocalDate requestedEnd = requestedStart.plusDays(requestedDays - 1);

        for (Rental r : rentals) {
            if (r.getVehicle().getRegistrationNumber().equalsIgnoreCase(vehicle.getRegistrationNumber())) {
                LocalDate rentalStart = r.getStartDate();
                LocalDate rentalEnd = r.getEndDate();

                if (!(requestedEnd.isBefore(rentalStart) || requestedStart.isAfter(rentalEnd))) {
                    return false;
                }
            }
        }

        return true;
    }
    public void createRental(String clientId, LocalDate startDate, int days,
            String category, int seats,
            ArrayList<AdditionalService> services, int pointsToRedeem) {

        Customer customer = findCustomerById(clientId);

        if (customer == null) {
            System.out.println("Δεν βρέθηκε πελάτης.");
            return;
        }

        Vehicle found = null;
        //polymorphism afou to v edomporei na einai EconomyCar, SUV, Convertible, LuxuryCar.
        for (Vehicle v : vehicles) {
            if (v.getCategory().equalsIgnoreCase(category)
                    && v.getSeats() >= seats
                    && isVehicleAvailable(v, startDate, days)) {
                found = v;
                break;
            }
        }

        if (found != null) {
            String rentalId = createRentalId();

            Rental rental = new Rental(rentalId, customer, found, startDate, days, services);

            double discount = customer.getLoyaltyAccount().redeemPoints(pointsToRedeem);
            rental.applyDiscount(discount);

            customer.getLoyaltyAccount().addPoints(rental.getTotalCost());

            rentals.add(rental);

            saveRentalsToFile();
            saveCustomersToFile();

            printInvoice(rentalId);

        } else {
            System.out.println("Δεν βρέθηκε διαθέσιμο " + category + ". Εναλλακτικά:");

            ArrayList<Vehicle> alternatives = new ArrayList<>();

            for (Vehicle v : vehicles) {
                if (v.getSeats() >= seats && isVehicleAvailable(v, startDate, days)) {
                    alternatives.add(v);
                }
            }
            
         // Polymorfismos ---i getDailyRate() kaleitai gia diaforetikous tipous oximaton kai kathe ipoklasi epistrefei diaforetiki timi enoikiasis.
         alternatives.sort(Comparator.comparingDouble(Vehicle::getDailyRate));
            

            for (Vehicle v : alternatives) {
                System.out.println(v.getCategory() + " - " + v.getRegistrationNumber()
                        + " - " + v.getDailyRate() + "€/μέρα");
            }
        }
    }
    public void createRentalFromUser(Scanner input) {
        System.out.println("Ταυτότητα πελάτη:");
        String client_id = input.nextLine().trim();

        if (!customerExists(client_id)) {
            System.out.println("Δεν βρέθηκε πελάτης με αυτό το ID.Κάνε πρώτα εγγραφή πελάτη.");
            return;
        }

        Customer customer = findCustomerById(client_id);

        LocalDate startDate = null;
        while (true) {
            try {
                System.out.println("Ημερομηνία έναρξης ενοικίασης: ( η μορφή της ημερομηνίας να είναι ως εξής YYYY-MM-DD )");
                String starting_date = input.nextLine().trim();
                startDate = LocalDate.parse(starting_date);
                break;
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Λάθος μορφή ημερομηνίας. Δοκίμασε ξανά!");
            }
        }

        int number_of_renting_days;
        while (true) {
            try {
                System.out.println("Διάρκεια ενοικίασης σε ημέρες:");
                number_of_renting_days = Integer.parseInt(input.nextLine().trim());

                if (number_of_renting_days <= 0) {
                    System.out.println("Η διάρκεια ενοικίασης πρέπει να είναι μεγαλύτερη από το 0.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Λάθος τιμή. Δώσε ακέραιο αριθμό ημερών.");
            }
        }

        System.out.println("Eπιθυμητή κατηγορία οχήματος:");
        String category = input.nextLine().trim();

        int number_of_seats;
        while (true) {
            try {
                System.out.println("Απαιτούμενος αριθμός θέσεων:");
                number_of_seats = Integer.parseInt(input.nextLine().trim());

                if (number_of_seats <= 0) {
                    System.out.println("Ο αριθμός θέσεων πρέπει να είναι μεγαλύτερος από το 0.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Λάθος τιμή. Δώσε ακέραιο αριθμό θέσεων.");
            }
        }

        ArrayList<AdditionalService> services = new ArrayList<>();

        System.out.println("Επιπρόσθετες υπηρεσίες ( π.χ. GPS , ChildSeat )");
        System.out.println("Γράψε NAI ή OXI");
        String additional_services = input.nextLine().trim().toUpperCase();

        if (additional_services.equals("NAI")) {
            System.out.println("GPS (ναι ή όχι):");
            String gps = input.nextLine().trim().toUpperCase();

            if (gps.equals("NAI")) {
                services.add(AdditionalService.GPS);
            }

            System.out.println("ChildSeat (ναι ή όχι):");
            String childSeat = input.nextLine().trim().toUpperCase();

            if (childSeat.equals("NAI")) {
                services.add(AdditionalService.CHILD_SEAT);
            }

            System.out.println("Extra Insurance (ναι ή όχι):");
            String extraInsurance = input.nextLine().trim().toUpperCase();

            if (extraInsurance.equals("NAI")) {
                services.add(AdditionalService.EXTRA_INSURANCE);
            }
        }

        int availablePoints = customer.getLoyaltyAccount().getPoints();
        int pointsToRedeem;

        while (true) {
            try {
                System.out.println("Διαθέσιμοι πόντοι: " + availablePoints);
                System.out.println("Θέλεις εξαργύρωση πόντων; Αν όχι γράψε 0 αλλιώς γράψε πόσους πόντους θέλεις να εξαργυρώσεις:");
                pointsToRedeem = Integer.parseInt(input.nextLine().trim());

                if (pointsToRedeem == 0) {
                    break;
                } else if (pointsToRedeem < 50) {
                    System.out.println("Η ελάχιστη εξαργύρωση είναι 50 πόντοι.");
                } else if (pointsToRedeem > availablePoints) {
                    System.out.println("Δεν έχεις τόσους διαθέσιμους πόντους.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Λάθος τιμή. Δώσε ακέραιο αριθμό πόντων.");
            }
        }

        createRental(client_id, startDate, number_of_renting_days, category, number_of_seats, services, pointsToRedeem);
    }
    public void printInvoice(String rentalId) {
        Rental rental = null;

        for (Rental r : rentals) {
            if (r.getRentalId().equalsIgnoreCase(rentalId)) {
                rental = r;
                break;
            }
        }

        if (rental == null) {
            System.out.println("Δεν βρέθηκε ενοικίαση με ID: " + rentalId);
            return;
        }
        
        System.out.println("           ΤΙΜΟΛΟΓΙΟ       ");
        System.out.println("===============================");
        System.out.println("Rental ID: " + rental.getRentalId());
        System.out.println("--- Στοιχεία Πελάτη ---");
        System.out.println("Όνομα: " + rental.getCustomer().getFullName());
        System.out.println("Ταυτότητα: " + rental.getCustomer().getIdNumber());
        System.out.println("--- Στοιχεία Οχήματος ---");
        System.out.println("Κατηγορία: " + rental.getVehicle().getCategory());
        System.out.println("Πινακίδα: " + rental.getVehicle().getRegistrationNumber());
        System.out.println("--- Κόστος ---");
        System.out.println("Ημερομηνία έναρξης: " + rental.getStartDate());
        System.out.println("Ημερομηνία λήξης: " + rental.getEndDate());
        System.out.println("Βασική χρέωση: " + rental.getBaseCost() + "€");
       
        if (rental.getServices().isEmpty()) {
            System.out.println("Καμία πρόσθετη υπηρεσία");
        } else {
            for (AdditionalService s : rental.getServices()) {
                System.out.println("  " + s + ": " + s.calculateCost(rental.getDurationDays()) + "€");
            }
        }
        System.out.println("Σύνολο υπηρεσιών: " + rental.getServicesCost() + "€");
        System.out.println("Έκπτωση: " + rental.getDiscount() + "€");
        System.out.println("Σύνολο: " + rental.getTotalCost() + "€");
        System.out.println("================================");
    }

    public void VehicleAvailability(LocalDate startDate, int numberOfDays, String category, int seats) {
        ArrayList<Vehicle> exactMatches = new ArrayList<>();
        ArrayList<Vehicle> alternativeMatches = new ArrayList<>();

        for (Vehicle v : vehicles) {
        	if (isVehicleAvailable(v, startDate, numberOfDays)) {
                if (v.getCategory().equalsIgnoreCase(category) && v.getSeats() >= seats) {
                    exactMatches.add(v);
                } else if (v.getSeats() >= seats) {
                    alternativeMatches.add(v);
                }
            }
        }
        
       // thimamai gia to comparator -gia kathe antikeimeno Vehicle pare to getDailyRate() pou epistrefei double kai taxinomise ta me vasi auti tin timi
        exactMatches.sort(Comparator.comparingDouble(Vehicle::getDailyRate));
        alternativeMatches.sort(Comparator.comparingDouble(Vehicle::getDailyRate));

        if (!exactMatches.isEmpty()) {
            System.out.println("Βρέθηκαν διαθέσιμα οχήματα:");
            for (Vehicle v : exactMatches) {
                System.out.println(v);
            }
        } else if (!alternativeMatches.isEmpty()) {
            System.out.println("Δεν βρέθηκε ακριβές όχημα στην κατηγορία " + category + ".");
            System.out.println("Εναλλακτικά διαθέσιμα οχήματα:");
            for (Vehicle v : alternativeMatches) {
                System.out.println(v);
            }
        } else {
            System.out.println("Δεν υπάρχουν διαθέσιμα οχήματα για τις συγκεκριμένες ημερομηνίες.");
        }
        System.out.println();
        System.out.println("--- ΜΗ ΔΙΑΘΕΣΙΜΑ ΟΧΗΜΑΤΑ ---");

        boolean foundUnavailable = false;

        for (Vehicle v : vehicles) {
            if (!isVehicleAvailable(v, startDate, numberOfDays)) {
                System.out.println(v);
                foundUnavailable = true;
            }
        }

        if (!foundUnavailable) {
            System.out.println("Όλα τα οχήματα είναι διαθέσιμα.");
        }
    }

    public void showCustomerHistory(String clientId) {
        try {
            Customer customer = findCustomerByIdOrThrow(clientId);

            System.out.println("=== ΙΣΤΟΡΙΚΟ ΠΕΛΑΤΗ ===");
            System.out.println("Όνομα: " + customer.getFullName());
            System.out.println("Ταυτότητα: " + customer.getIdNumber());
            System.out.println("Πόντοι: " + customer.getLoyaltyAccount().getPoints());
            System.out.println("--- Ενοικιάσεις ---");

            for (Rental r : rentals) {
                if (r.getCustomer().getIdNumber().equalsIgnoreCase(clientId)) {
                    System.out.println("Rental ID: " + r.getRentalId());
                    System.out.println("  Όχημα: " + r.getVehicle().getBrand()
                            + " " + r.getVehicle().getModel());
                    System.out.println("  Από: " + r.getStartDate()
                            + " έως: " + r.getEndDate());
                    System.out.println("  Κόστος: " + r.getTotalCost() + "€");
                    System.out.println("--------------------------------");
                }
            }

        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showVehicleHistory(String regNumber) {
        Vehicle vehicle = findVehicleByRegistration(regNumber);

        if (vehicle == null) {
            System.out.println("Δεν βρέθηκε όχημα.");
            return;
        }

        System.out.println("=== ΙΣΤΟΡΙΚΟ ΟΧΗΜΑΤΟΣ ===");
        System.out.println("Κατηγορία: " + vehicle.getCategory());
        System.out.println("Πινακίδα: " + vehicle.getRegistrationNumber());
        System.out.println("Μάρκα: " + vehicle.getBrand());
        System.out.println("Μοντέλο: " + vehicle.getModel());

        for (Rental r : rentals) {
            if (r.getVehicle().getRegistrationNumber().equalsIgnoreCase(regNumber)) {
                System.out.println("Rental ID: " + r.getRentalId());
                System.out.println("  Πελάτης: " + r.getCustomer().getFullName());
                System.out.println("  Από: " + r.getStartDate() + " έως: " + r.getEndDate());
                System.out.println("  Κόστος: " + r.getTotalCost() + "€");
                System.out.println("-------------------------------------------------");
            }
        }
    }

    public void printAllData() {
    	System.out.println();
        System.out.println("=== ΟΧΗΜΑΤΑ ===");
        for (Vehicle v : vehicles) {
            System.out.println(v.getCategory() + " - " + v.getRegistrationNumber()
                    + " - " + v.getBrand() + " " + v.getModel());
        }
        System.out.println();
        System.out.println("=== ΠΕΛΑΤΕΣ ===");
        for (Customer c : customers) {
            System.out.println(c.getFullName() + " - " + c.getIdNumber());
        }
        System.out.println();
        System.out.println("=== ΕΝΟΙΚΙΑΣΕΙΣ ===");
        for (Rental r : rentals) {
            System.out.println(r.getRentalId() + " - " + r.getCustomer().getFullName()
                    + " - " + r.getVehicle().getRegistrationNumber());
        }
    }
    
    public Customer findCustomerByIdOrThrow(String clientId) throws CustomerNotFoundException {
        for (Customer c : customers) {
        	if (c.getIdNumber().equalsIgnoreCase(clientId)) {
        	    return c;
        	}
        }
        throw new CustomerNotFoundException("Δεν βρέθηκε πελάτης με ID: " + clientId);
    }
    
    
    
    
}