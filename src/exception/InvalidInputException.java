package exception;

/**
 * Custom checked exception for user input issues in the menu layer.
 * Example: empty input, wrong confirmation, etc.
 * <p>
 * Model setters use IllegalArgumentException (unchecked) for invalid data.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
