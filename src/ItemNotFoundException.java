/**
 * Thrown when an operation references an item ID that does not exist.
 * Java Concept: Exception Handling (unknown item)
 */
public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
