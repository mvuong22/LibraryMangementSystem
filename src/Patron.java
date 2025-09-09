import java.math.BigDecimal;

// Class Patron stores all info about a library patron
public class Patron {

    // 7-digit ID
    private final String id;

    // Patron name
    private final String name;

    // Patron address
    private final String address;

    // Fine amount between 0-250
    private final BigDecimal fine;

    // Constructor to create a new patron
    public Patron(String id, String name, String address, BigDecimal fine) {

        // Check that ID is exactly 7 digits
        if (!id.matches("\\d{7}")) throw new IllegalArgumentException("ID must be 7 digits.");

        // Check name is not empty
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");

        // Check address is not empty
        if (address.isEmpty()) throw new IllegalArgumentException("Address cannot be empty.");

        // Check fine is in range 0-250
        if (fine.compareTo(BigDecimal.ZERO) < 0 || fine.compareTo(new BigDecimal("250")) > 0)
            throw new IllegalArgumentException("Fine must be between 0 and 250.");

        this.id = id;
        this.name = name;
        this.address = address;

        // Round fine to 2 decimal places
        this.fine = fine.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // Getter for ID
    public String getId() { return id; }

    // Getter for name
    public String getName() { return name; }

    // Getter for address
    public String getAddress() { return address; }

    // Getter for fine
    public BigDecimal getFine() { return fine; }

    // Display patron info in a table format
    @Override
    public String toString() {
        return String.format("%-10s | %-20s | %-30s | %6.2f", id, name, address, fine);
    }
}
