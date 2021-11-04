package src;

public class TooFewValuesException extends Exception{
    /**
	 * Constructs an instance of the exception with no message
	 */
	public TooFewValuesException() {
		// do nothing
	}

	/**
	 * Constructs an instance of the exception containing the message argument
	 * 
	 * @param message message containing details regarding the exception cause
	 */
	public TooFewValuesException(String message) {
		super(message);
	}
}
