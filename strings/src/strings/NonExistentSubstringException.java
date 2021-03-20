package strings;

public class NonExistentSubstringException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonExistentSubstringException(String message) {
		super(message);
	}
}
