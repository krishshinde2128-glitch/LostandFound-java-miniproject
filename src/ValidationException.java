/**
 * Thrown when input data (item ID, date, claimant details, etc.) fails validation.
 * Java Concept: Validation + Exception Handling
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
