package strings.regexp;

public abstract class MatchableGroupableObject extends MatchableObject {
	
	private final int originalLength;

	public MatchableGroupableObject(String str, RegExpBuilder builder) {
		super(str, builder);
		originalLength = str.length();
	}

	@Override
	protected RegExpBuilder appendString() {
		return getRegExpBuilder()
			.append(originalLength == 1 ? getString() : group().getString());
	}

}