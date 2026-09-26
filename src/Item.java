import java.time.LocalDate;

/**
 * Abstract base class representing any item involved in the
 * Lost and Found system. LostItem and FoundItem extend this class.
 *
 * Demonstrates: Classes & Objects, Constructors, Arrays (static constants)
 */
public abstract class Item {

    // Array of allowed categories (Java Concept: Array)
    public static final String[] CATEGORIES = {
            "Electronics", "Books", "Bags", "Documents", "Accessories", "Others"
    };

    // Array of allowed status values (Java Concept: Array)
    public static final String[] STATUSES = {
            "LOST", "FOUND", "CLAIMED", "RETURNED"
    };

    protected String itemId;
    protected String description;
    protected String category;
    protected String location;
    protected LocalDate date;
    protected String status;

    public Item(String itemId, String description, String category,
                String location, LocalDate date, String status) {
        this.itemId = itemId;
        this.description = description;
        this.category = category;
        this.location = location;
        this.date = date;
        this.status = status;
    }

    public static boolean isValidCategory(String category) {
        for (String c : CATEGORIES) {
            if (c.equalsIgnoreCase(category)) return true;
        }
        return false;
    }

    public static boolean isValidStatus(String status) {
        for (String s : STATUSES) {
            if (s.equalsIgnoreCase(status)) return true;
        }
        return false;
    }

    public String getItemId() { return itemId; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public LocalDate getDate() { return date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /** Each subclass reports its own type label (LOST / FOUND). */
    public abstract String getTypeLabel();

    /** Each subclass exposes the name of the person associated with it. */
    public abstract String getContactPersonName();

    /** Each subclass exposes the contact info of the person associated with it. */
    public abstract String getContactPersonInfo();

    @Override
    public String toString() {
        return String.format("%-6s | %-6s | %-11s | %-25s | %-15s | %-11s | %-9s",
                itemId, getTypeLabel(), category, truncate(description, 25),
                truncate(location, 15), date, status);
    }

    protected static String truncate(String s, int len) {
        if (s == null) return "";
        return s.length() <= len ? s : s.substring(0, len - 3) + "...";
    }
}
