package src;

public class NegativeWeightException extends Exception {

	/**
	 * Constructs an instance of the exception with no message
	 */
	public NegativeWeightException() {
		// do nothing
	}

	/**
	 * Constructs an instance of the exception containing the message argument
	 * 
	 * @param message message containing details regarding the exception cause
	 */
	public NegativeWeightException(String message) {
		super(message);
	}
}
