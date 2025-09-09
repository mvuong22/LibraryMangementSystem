import java.math.BigDecimal;
import java.util.*;

// Class MenuUI handles all user input and displays menu
public class MenuUI {

    private final Scanner scanner = new Scanner(System.in);
    private final AppController controller;

    public MenuUI(AppController controller) {
        this.controller = controller;
    }

    // Main menu loop
    public void displayMenu() {
        while (true) {
            // Print menu
            System.out.println("\n--------------------------------------------------");
            System.out.println("           Library Management System             ");
            System.out.println("--------------------------------------------------\n");
            // Select menu options list
            System.out.println("1. Add Patron");
            System.out.println("2. Remove Patron");
            System.out.println("3. Import Patrons");
            System.out.println("4. List All Patrons");
            System.out.println("5. Search Patron");
            System.out.println("6. Exit\n");
            System.out.print(">Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": handleAddPatron(); break;
                case "2": handleRemovePatron(); break;
                case "3": handleImportPatrons(); break;
                case "4": handleListPatrons(); break;
                case "5": handleSearchPatron(); break;
                case "6": System.out.println("Exiting..."); return;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void handleAddPatron() {
        // validate ID first
        String id;
        while (true) {
            System.out.print("Enter 7-digit ID: ");
            id = scanner.nextLine().trim();
            if (id.matches("\\d{7}")) break;
            System.out.println("Error! Invalid ID, must be 7 digits.");
        }

        // validate name next
        String name;
        while (true) {
            System.out.print("Enter name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty() && !name.matches(".*\\d.*")) break;
            System.out.println("Error! Invalid, name cannot be empty or contain numbers.");
        }

        // validate address
        String address;
        while (true) {
            System.out.print("Enter address: ");
            address = scanner.nextLine().trim();
            if (!address.isEmpty()) break;
            System.out.println("Warning! Invalid address. Cannot be empty.");
        }

        // validate fine
        BigDecimal fine;
        while (true) {
            System.out.print("Enter fine (0-250): ");
            String fineInput = scanner.nextLine().trim();
            try {
                fine = new BigDecimal(fineInput);
                if (fine.compareTo(BigDecimal.ZERO) >= 0 && fine.compareTo(new BigDecimal("250")) <= 0)
                    break;
                else
                    System.out.println("Warning! Fine must be between $0-250.");
            } catch (NumberFormatException e) {
                System.out.println("Error, fine must be a number!");
            }
        }

        // try to add patron
        if (controller.addPatron(id, name, address, fine)) {
            System.out.println("\nSuccess! Patron added.");
        } else {
            System.out.println("\nFailed! Duplicate ID.");
        }
    }

    // Remove patron with retry
    private void handleRemovePatron() {
        while (true) {
            System.out.print("Enter patron ID to remove: ");
            String id = scanner.nextLine().trim();
            if (controller.removePatron(id)) {
                System.out.println("\nPatron removed successfully!");
                break;
            } else {
                System.out.println("\nError! No patron found with that ID. Please try again!");
            }
        }
    }

    // Import patrons from file
    private void handleImportPatrons() {
        System.out.print("Enter filename to import: ");
        String filename = scanner.nextLine();
        int count = controller.importFromFile(filename);
        System.out.println("\n"+ count + " patrons imported successfully.");
    }

    // List all patrons
    private void handleListPatrons() {
        List<Patron> all = controller.listPatrons();
        if (all.isEmpty()) {
            System.out.println("\nError! No patrons found. Please add.");
        } else {
            System.out.printf("%-10s | %-20s | %-30s | %6s\n", "ID", "Name", "Address", "Fine");
            System.out.println("----------------------------------------------------------------------------------");
            for (Patron p : all) System.out.println(p);
        }
    }

    // Search patron by ID or name with retry and exit option
    private void handleSearchPatron() {
        while (true) {
            System.out.print("Search by ID or Name (or type 'EXIT' to go back to menu): ");
            String choice = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(choice)) {
                System.out.println("\nReturning to main menu...");
                break; // exit search
            }

            if ("ID".equalsIgnoreCase(choice)) {
                System.out.print("Enter ID: ");
                String id = scanner.nextLine().trim();
                if ("EXIT".equalsIgnoreCase(id)) continue; // go back to search type choice
                Patron p = controller.searchById(id);
                if (p != null) {
                    System.out.println("\nPatron found:");
                    System.out.printf("%-10s | %-20s | %-30s | %6s\n", "ID", "Name", "Address", "Fine");
                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println(p);
                    break; // found, exit search
                } else {
                    System.out.println("\nWarning! No patron found with that ID. Try again.");
                }
            } else if ("Name".equalsIgnoreCase(choice)) {
                System.out.print("Enter Name: ");
                String name = scanner.nextLine().trim();
                if ("EXIT".equalsIgnoreCase(name)) continue; // back to search type choice
                List<Patron> results = controller.searchByName(name);
                if (!results.isEmpty()) {
                    System.out.println("\nSearch results:");
                    System.out.printf("%-10s | %-20s | %-30s | %6s\n", "ID", "Name", "Address", "Fine");
                    System.out.println("----------------------------------------------------------------------------------");
                    results.forEach(System.out::println);
                    break; // found, exit search
                } else {
                    System.out.println("\nWarning! No patrons found with that name. Try again.");
                }
            } else {
                System.out.println("\nInvalid choice. Type 'ID', 'Name', or 'exit'.");
            }
        }
    }

    public static void main(String[] args) {
        PatronRepository repo = new PatronRepository();
        AppController controller = new AppController(repo);
        MenuUI menu = new MenuUI(controller);
        menu.displayMenu();
    }
}
