package strings.regexp;

public final class MatchableString extends MatchableGroupableObject {

	MatchableString(String str, RegExpBuilder builder) {
		super(str, builder);
	}
	
	@Override
	protected RegExpBuilder appendString() {
		setString(escape(getString()));
		return super.appendString();
	}
	
	private static String escape(String str) {
		return str.replaceAll("([\\/\\(\\)\\[\\]\\{\\}?^$.+*|])", "\\\\$1");
	}
	
}
