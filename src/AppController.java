import java.io.*;
import java.math.BigDecimal;
import java.util.List;

// Class AppController connects menu with repository and handles logic
public class AppController {

    // Repository stores all patrons
    private final PatronRepository repository;

    // Constructor
    public AppController(PatronRepository repository) {
        this.repository = repository;
    }

    // Add a new patron
    public boolean addPatron(String id, String name, String address, BigDecimal fine) {
        try {
            Patron p = new Patron(id, name, address, fine);
            return repository.addPatron(p);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Remove a patron by ID
    public boolean removePatron(String id) {
        return repository.removePatron(id);
    }

    // List all patrons
    public List<Patron> listPatrons() {
        return repository.getAllPatrons();
    }

    // Search by ID
    public Patron searchById(String id) {
        return repository.searchById(id);
    }

    // Search by name
    public List<Patron> searchByName(String name) {
        return repository.searchByName(name);
    }

    // Import patrons from a file
    public int importFromFile(String filename) {
        int count = 0;

        // Try reading the file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            // Read each line
            while ((line = br.readLine()) != null) {

                // Split line into parts
                String[] parts = line.split("-");
                if (parts.length != 4) continue;

                // Get each value
                String id = parts[0].trim();
                String name = parts[1].trim();
                String address = parts[2].trim();
                BigDecimal fine;

                // Check fine is a number
                try {
                    fine = new BigDecimal(parts[3].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid fine value for ID " + id);
                    continue;
                }

                // Add patron
                if (addPatron(id, name, address, fine)) count++;
            }

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }

        return count;
    }
}
