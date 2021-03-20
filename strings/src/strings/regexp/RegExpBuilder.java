package strings.regexp;

import java.util.function.Supplier;
import java.util.regex.PatternSyntaxException;

import generalutils.ConsoleWarning;
import strings.regexp.escapechars.RegExpEscapeCharacter;

/*
 TODO 
 .
 ? as non greedy
 lookaround
 backreference
 */

public final class RegExpBuilder {
	
	private StringBuilder regexp;
	private boolean openedGroup = false;

	public RegExpBuilder() {
		regexp = new StringBuilder();
	}
	
	public RegExpBuilder(RegExpBuilder start) {
		regexp = new StringBuilder(start.build());
	}
	
	RegExpBuilder append(String str) {
		regexp.append(str);
		return this;
	}
	
	public MatchableGroupableObject matchAny() {
		return new MatchableGroupableObject(".", this) {
		};
	}
	
	public MatchableString match(String str) {
		return new MatchableString(str, this);
	}
	
	public MatchableRegExpBuilder match(RegExpBuilder subBuilder) {
		return new MatchableRegExpBuilder(subBuilder, this);
	}
	
	public MatchableRegExpBuilder match(Supplier<RegExpBuilder> builderSupplier) {
		return match(builderSupplier.get());
	}
	
	public MatchableEscapeCharacter match(RegExpEscapeCharacter escapeChar) {
		return new MatchableEscapeCharacter(escapeChar, this);
	}

	public MatchableCharacterClass matchAnyOf(String chars) {
		return new MatchableCharacterClass(chars, this);
	}
	
	public MatchableCharacterClass matchAnyOf(String chars, CharacterClassRange range) {
		return new MatchableCharacterClass(chars, range, this);
	}
	
	public MatchableCharacterClass matchAnyOf(CharacterClassRange range) {
		return new MatchableCharacterClass(range, this);
	}
	
	public static CharacterClassRange range(String range) {
		return new CharacterClassRange(range);
	}
	
	public MatchableCharacterClass matchAnyOf(RegExpEscapeCharacter... escapeChars) {
		return new MatchableCharacterClass(this, escapeChars);
	}
	
	public MatchableOrCondition matchAnyOf(String... strings) {
		return new MatchableOrCondition(this, strings);
	}
	
	public MatchableOrCondition matchAnyOf(RegExpBuilder... subBuilders) {
		return new MatchableOrCondition(this, subBuilders);
	}
	
	public RegExpBuilder startGroup() {
		openedGroup = true;
		return append("(");
	}
	
	public RegExpBuilder endGroup() {
		if (!openedGroup) {
			new ConsoleWarning("endGroup() was called before calling startGroup()");
			return this;
		}
		
		openedGroup = false;
		return append(")");
	}
	
	public String build() {
		String built = regexp.toString();
		if (openedGroup)
			throw new PatternSyntaxException("One or more groups were opened but never closed", built, -1);
		return built;
	}
	
	@Override
	public String toString() {
		return build();
	}

}
