import java.time.LocalDate;

/**
 * Represents an item reported as LOST by a student.
 * Java Concept: Classes & Objects, Constructors (inheritance)
 */
public class LostItem extends Item {

    private String reporterName;
    private String reporterContact;

    public LostItem(String itemId, String description, String category, String location,
                     LocalDate date, String reporterName, String reporterContact) {
        super(itemId, description, category, location, date, "LOST");
        this.reporterName = reporterName;
        this.reporterContact = reporterContact;
    }

    public String getReporterName() { return reporterName; }
    public String getReporterContact() { return reporterContact; }

    @Override
    public String getTypeLabel() { return "LOST"; }

    @Override
    public String getContactPersonName() { return reporterName; }

    @Override
    public String getContactPersonInfo() { return reporterContact; }
}
