package strings.regexp;

public final class MatchableRegExpBuilder extends MatchableGroupableObject {

	MatchableRegExpBuilder(RegExpBuilder subBuilder, RegExpBuilder builder) {
		super(subBuilder.build(), builder);
	}

}
