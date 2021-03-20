package chars;

public class Characters {
	
	public final static int ALPHABET_LENGTH = 26;
	public final static int ITALIAN_ALPHABET_LENGTH = 21;
	public final static int LOWERCASE_A_ASCII = 97;
	public final static int UPPERCASE_A_ASCII = 65;
	
	/**
	 * @param ch
	 * @return the corresponding index of {@code ch} in the alphabet
	 * @exception IllegalArgumentException if {@code ch} is not a letter
	 */
	public static int getIndexInAlphabet(char ch) {
		checkLetter(ch);
		if (Character.isUpperCase(ch))
			return ch - UPPERCASE_A_ASCII;
		return ch - LOWERCASE_A_ASCII;
	}

	public static char increaseLetter(char ch, int by) {
		checkLetter(ch);
		if (Character.isUpperCase(ch))
			return (char) ((ch + by - UPPERCASE_A_ASCII) % ALPHABET_LENGTH + UPPERCASE_A_ASCII);
		return (char) ((ch + by - LOWERCASE_A_ASCII) % ALPHABET_LENGTH + LOWERCASE_A_ASCII);
	}
	
	public static char decreaseLetter(char ch, int by) {
		return increaseLetter(ch, -by);
	}
	
	/**
	 * Returns the letter selected by counting up from the bottom of the alphabet
	 * the same index as {@code ch} (calculated via {@link #getIndexInAlphabet(ch)}).
	 * For example, passing 'a' would give 'z' and passing 'X' would give 'C'. 
	 * 
	 * @param ch
	 * @return
	 * @exception IllegalArgumentException if {@code ch} is not a letter
	 */
	public static char getCorrespondingOnInvertingAlphabet(char ch) {
		checkLetter(ch);
		if (Character.isUpperCase(ch))
			return decreaseLetter('Z', getIndexInAlphabet(ch));
		return decreaseLetter('z', getIndexInAlphabet(ch));
	}
	
	private static void checkLetter(char ch) {
		if (!Character.isLetter(ch))
			throw new IllegalArgumentException(ch + " is not a letter");
	}

}
