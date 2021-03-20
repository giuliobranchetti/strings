package strings.regexp.escapechars;

public class RegExpEscapeCharacters {

	private RegExpEscapeCharacters() {
	}
	
	public static final RegExpEscapeCharacter 	    WHITESPACE = new RegExpEscapeCharacter('s');
	public static final RegExpEscapeCharacter   NOT_WHITESPACE = new RegExpEscapeCharacter('S');
	public static final RegExpEscapeCharacter         FORMFEED = new RegExpEscapeCharacter('f');
	public static final RegExpEscapeCharacter         LINEFEED = new RegExpEscapeCharacter('n');
	public static final RegExpEscapeCharacter  CARRIAGE_RETURN = new RegExpEscapeCharacter('r');
	public static final RegExpEscapeCharacter              TAB = new RegExpEscapeCharacter('t');
	public static final RegExpEscapeCharacter     VERTICAL_TAB = new RegExpEscapeCharacter('v');
	public static final RegExpEscapeCharacter            DIGIT = new RegExpEscapeCharacter('d');
	public static final RegExpEscapeCharacter        NOT_DIGIT = new RegExpEscapeCharacter('D');
	public static final RegExpEscapeCharacter     ALPHANUMERIC = new RegExpEscapeCharacter('w');
	public static final RegExpEscapeCharacter NOT_ALPHANUMERIC = new RegExpEscapeCharacter('W');

}
