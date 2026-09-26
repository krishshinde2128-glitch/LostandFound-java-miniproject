import java.time.LocalDate;

/**
 * Represents a claim filed by a student for a found item,
 * and tracks ownership verification + the return process.
 * Java Concept: Classes & Objects, Constructors
 */
public class Claim {

    private String claimId;
    private String itemId;
    private Student claimant;
    private LocalDate claimDate;
    private boolean verified;
    private boolean returned;

    public Claim(String claimId, String itemId, Student claimant, LocalDate claimDate) {
        this.claimId = claimId;
        this.itemId = itemId;
        this.claimant = claimant;
        this.claimDate = claimDate;
        this.verified = false;
        this.returned = false;
    }

    public String getClaimId() { return claimId; }
    public String getItemId() { return itemId; }
    public Student getClaimant() { return claimant; }
    public LocalDate getClaimDate() { return claimDate; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }

    @Override
    public String toString() {
        return String.format("%-8s | %-6s | %-20s | %-11s | Verified:%-5s | Returned:%-5s",
                claimId, itemId, claimant.getName(), claimDate, verified, returned);
    }
}
