package strings.regexp;

import java.util.Arrays;
import java.util.stream.Collectors;

import strings.regexp.escapechars.RegExpEscapeCharacter;

public final class MatchableCharacterClass extends MatchableObject {

	MatchableCharacterClass(String chars, RegExpBuilder builder) {
		super("[" + escapeForCharacterClass(chars) + "]", builder);
	}
	
	MatchableCharacterClass(RegExpBuilder builder, RegExpEscapeCharacter... escapeChars) {
		this(
			Arrays.stream(escapeChars)
				.map(RegExpEscapeCharacter::toString)
				.collect(Collectors.joining()),
			builder
		);
	}
	
	MatchableCharacterClass(String chars, CharacterClassRange range, RegExpBuilder builder) {
		super("[" + escapeForCharacterClass(chars) + range.getString() + "]", builder);	
	}
	
	MatchableCharacterClass(CharacterClassRange range, RegExpBuilder builder) {
		super("[" + range.getString() + "]", builder);	
	}
	
	// solo parentesi tonde, quadre, slash e -
	private static String escapeForCharacterClass(String str) {
		return str.replaceAll("([\\/\\(\\)\\[\\]-])", "\\\\$1");
	}

}
