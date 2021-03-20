package strings.regexp;

import java.util.regex.PatternSyntaxException;

final class CharacterClassRange {
	
	private final String range;

	CharacterClassRange(String range) {
		if (range.isBlank() || !range.matches("([A-Z]-[A-Za-z])*([a-z]-[a-z])*([0-9]-[0-9])*"))
			throw new PatternSyntaxException("Illegal character class range", range, -1);

		char rangeFirstChar = 0;
		boolean rangeStarted = false;
		
		for (int i = 0; i < range.length(); i++) {
			char ch = range.charAt(i);
			
			if (!rangeStarted) {
				rangeFirstChar = ch;
				rangeStarted = true;
			}
			else {
				if (ch == '-')
					continue;
				
				if (ch < rangeFirstChar)
					throw new PatternSyntaxException("Out of order character class range", range, i);
				
				rangeStarted = false;
			}
		}
		
		this.range = range;
	}
	
	public String getString() {
		return range;
	}

}
