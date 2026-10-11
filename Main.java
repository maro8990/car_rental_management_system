package java_project;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void printMenu() {
    	System.out.println();
        System.out.println("Σύστημα Διαχείρισης Ενοικίασης Αυτοκινήτων");
        System.out.println("Επιλογές:");
        System.out.println("1. Διαχείριση Οχήματος (Εισαγωγή/Διαγραφή)");
        System.out.println("2. Διαχείριση Πελάτη (Εγγραφή/Αναζήτηση)");
        System.out.println("3. Δημιουργία Ενοικίασης");
        System.out.println("4. Εκτύπωση Τιμολογίου");
        System.out.println("5. Εμφάνιση Διαθεσιμότητας Οχημάτων");
        System.out.println("6. Εμφάνιση Ιστορικού Πελάτη");
        System.out.println("7. Εμφάνιση Ιστορικού Οχήματος");
        System.out.println("8. Εκτύπωση Όλων των Δεδομένων");
        System.out.println("0. Έξοδος");
        System.out.print("Επέλεξε λειτουργικότητα: ");
    }

    
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        CarRentalSystem system = new CarRentalSystem();

        system.loadVehiclesFromFile();
        system.loadCustomersFromFile();
        system.loadRentalsFromFile();

        int choice = -1;
        String epilogi_oximatos = "";
        String epilogi_pelatis = "";
        String client_id = "";

        while (choice != 0) {
            printMenu();
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                	System.out.println();
                	System.out.println();
                    System.out.println("Επέλεξες: Διαχείριση Οχήματος(Εισαγωγή/Διαγραφή)");
                    System.out.println("Για εισαγωγή γράψε Ε για διαγραφή γράψε Δ:");
                    epilogi_oximatos = input.nextLine().toUpperCase();

                    if (epilogi_oximatos.equals("Ε")) {
                        system.addVehicle(input);
                    } else if (epilogi_oximatos.equals("Δ")) {
                        system.removeVehicle(input);
                    } else {
                        System.out.println("Κάτι έγραψες λάθος.Δοκίμασε ξανά!");
                    }
                    break;

                case 2:
                	System.out.println();
                	System.out.println();
                    System.out.println("Επέλεξες: Διαχείριση Πελάτη (Εγγραφή/Αναζήτηση)");
                    System.out.println("Για εγγραφή γράψε Ε, για αναζήτηση γράψε Α:");
                    epilogi_pelatis = input.nextLine().toUpperCase();

                    if (epilogi_pelatis.equals("Ε")) {
                        System.out.println("Ταυτότητα χρήστη:");
                        client_id = input.nextLine();

                        if (system.customerExists(client_id)) {
                            System.out.println("Υπάρχει ήδη πελάτης με αυτό το ID.");
                        } else {
                            System.out.println("Δεν βρέθηκε άλλος πελάτης με αυτό το ID.Mπορείς να τον προσθέσεις.");
                            system.registerCustomer(input, client_id);
                        }
                    } else if (epilogi_pelatis.equals("Α")) {
                        System.out.println("Δώσε ταυτότητα πελάτη:");
                        client_id = input.nextLine();
                        system.showCustomerHistory(client_id);
                    } else {
                        System.out.println("Κάτι έγραψες λάθος.Δοκίμασε ξανά!");
                    }
                    break;

                case 3:
                	System.out.println();
                	System.out.println();
                    System.out.println("Επέλεξες: Δημιουργία Ενοικίασης");
                    system.createRentalFromUser(input);
                    break;

                case 4:
                	System.out.println();
                	System.out.println();
                    System.out.println("Επέλεξες: Εκτύπωση Τιμολογίου");
                    System.out.println("Δώσε Rental ID:");
                    String rentalId = input.nextLine();
                    system.printInvoice(rentalId);
                    break;

                case 5:
                	System.out.println();
                	System.out.println();
                    System.out.println("Επέλεξες: Εμφάνιση Διαθεσιμότητας Οχημάτων");

                    System.out.println("Ημερομηνία έναρξης:");
                    String start = input.nextLine();

                    System.out.println("Διάρκεια ενοικίασης σε ημέρες:");
                    int days = input.nextInt();
                    input.nextLine();

                    System.out.println("Επιθυμητή κατηγορία οχήματος:");
                    String category = input.nextLine();

                    System.out.println("Απαιτούμενος αριθμός θέσεων:");
                    int seats = input.nextInt();
                    input.nextLine();

                    system.VehicleAvailability(LocalDate.parse(start), days, category, seats);
                    break;

                case 6:
                	System.out.println();
                	System.out.println();
                    System.out.println("Επέλεξες: Εμφάνιση Ιστορικού Πελάτη");
                    System.out.println("Δώσε ταυτότητα πελάτη:");
                    client_id = input.nextLine();
                    system.showCustomerHistory(client_id);
                    break;

                case 7:
                	System.out.println();
                	System.out.println();
                    System.out.println("Επέλεξες: Εμφάνιση Ιστορικού Οχήματος");
                    System.out.println("Δώσε αριθμό εγγραφής οχήματος:");
                    String regNumber = input.nextLine();
                    system.showVehicleHistory(regNumber);
                    break;

                case 8:
                	System.out.println();
                	System.out.println();
                    System.out.println("Επέλεξες: Εκτύπωση Όλων των Δεδομένων");
                    system.printAllData();
                    break;

                case 0:
                	System.out.println();
                	System.out.println();
                    System.out.println("Έξοδος από το πρόγραμμα...");
                    break;

                default:
                	System.out.println();
                	System.out.println();
                    System.out.println("Μη έγκυρη επιλογή. Προσπάθησε ξανά.");
            }
        }

     
    }
}