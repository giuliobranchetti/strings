package strings.regexp.escapechars;

public final class RegExpEscapeCharacter {

	private final char symbol;
	
	RegExpEscapeCharacter(char symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public String toString() {
		return "\\" + symbol;
	}

}
