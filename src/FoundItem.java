import java.time.LocalDate;

/**
 * Represents an item reported as FOUND on campus by someone.
 * Java Concept: Classes & Objects, Constructors (inheritance)
 */
public class FoundItem extends Item {

    private String finderName;
    private String finderContact;

    public FoundItem(String itemId, String description, String category, String location,
                      LocalDate date, String finderName, String finderContact) {
        super(itemId, description, category, location, date, "FOUND");
        this.finderName = finderName;
        this.finderContact = finderContact;
    }

    public String getFinderName() { return finderName; }
    public String getFinderContact() { return finderContact; }

    @Override
    public String getTypeLabel() { return "FOUND"; }

    @Override
    public String getContactPersonName() { return finderName; }

    @Override
    public String getContactPersonInfo() { return finderContact; }
}
