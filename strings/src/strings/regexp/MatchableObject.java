package strings.regexp;

import strings.Strings;

public abstract class MatchableObject {
	
	private String str;
	private final RegExpBuilder builder;

	public MatchableObject(String str, RegExpBuilder builder) {
		this.str = str;
		this.builder = builder;
	}
	
	public MatchableObject group() {
		str = Strings.prepend(str, "(");
		str = Strings.append(str, ")");
		return this;
	}
	
	protected void setString(String str) {
		this.str = str;
	}
	
	protected String getString() {
		return str;
	}
	
	protected RegExpBuilder getRegExpBuilder() {
		return builder;
	}
	
	protected RegExpBuilder appendString() {
		return builder.append(str);
	}
	
	public RegExpBuilder once() {
		return builder.append(str);
	}
	
	public RegExpBuilder times(int times) {
		checkPositive(times);
		
		appendString();
		return builder.append("{" + times + "}");
	}
	
	public RegExpBuilder times(int from, int to) {
		checkPositive(from);
		if (to < from)
			throw new IllegalArgumentException("to must be bigger than from");
		
		appendString();
		return builder.append("{" + from + "," + to + "}");
	}
	
	public RegExpBuilder atLeastTimes(int from) {
		checkPositive(from);
		
		appendString();
		return builder.append("{" + from + ",}");
	}
	
	public RegExpBuilder zeroOrOneTimes() {
		appendString();
		return builder.append("?");
	}
	
	public RegExpBuilder zeroOrMoreTimes() {
		appendString();
		return builder.append("*");
	}
	
	public RegExpBuilder oneOrMoreTimes() {
		appendString();
		return builder.append("+");
	}
	
	private void checkPositive(int n) {
		if (n < 0)
			throw new IllegalArgumentException("A times number must be positive");
	}

}
