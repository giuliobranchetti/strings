package strings.regexp;

import strings.regexp.escapechars.RegExpEscapeCharacter;

public final class MatchableEscapeCharacter extends MatchableObject {

	MatchableEscapeCharacter(RegExpEscapeCharacter escapeChar, RegExpBuilder builder) {
		super(escapeChar.toString(), builder);
	}

}
