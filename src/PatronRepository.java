import java.util.*;
import java.util.stream.Collectors;

// Class PatronRepository stores all patrons in memory
public class PatronRepository {

    // Map to store patrons with ID as key
    private final Map<String, Patron> patrons = new HashMap<>();

    // Add a patron if ID is not already used
    public boolean addPatron(Patron p) {
        if (patrons.containsKey(p.getId())) return false;
        patrons.put(p.getId(), p);
        return true;
    }

    // Remove a patron by ID
    public boolean removePatron(String id) {
        return patrons.remove(id) != null;
    }

    // Get all patrons sorted by ID
    public List<Patron> getAllPatrons() {
        return patrons.values().stream()
                .sorted(Comparator.comparing(Patron::getId))
                .collect(Collectors.toList());
    }

    // Search patron by ID
    public Patron searchById(String id) {
        return patrons.get(id);
    }

    // Search patrons by name (partial match)
    public List<Patron> searchByName(String name) {
        return patrons.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(Patron::getId))
                .collect(Collectors.toList());
    }
}
