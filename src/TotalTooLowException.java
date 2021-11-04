package src;

public class TotalTooLowException extends Exception {

	/**
	 * Constructs an instance of the exception with no message
	 */
	public TotalTooLowException() {
		// do nothing
	}

	/**
	 * Constructs an instance of the exception containing the message argument
	 * 
	 * @param message message containing details regarding the exception cause
	 */
	public TotalTooLowException(String message) {
		super(message);
	}
}
