/**
 * Thrown when a claim operation is invalid, e.g. claiming an item that
 * is not FOUND, verifying/returning a non-existent or already-processed claim.
 * Java Concept: Exception Handling (invalid claim)
 */
public class InvalidClaimException extends Exception {
    public InvalidClaimException(String message) {
        super(message);
    }
}
