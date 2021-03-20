package strings.regexp;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class MatchableOrCondition extends MatchableObject {

	MatchableOrCondition(RegExpBuilder builder, String... strings) {
		super(createOrCondition((Object[]) strings), builder);
	}
	
	MatchableOrCondition(RegExpBuilder builder, RegExpBuilder... builders) {
		super(createOrCondition((Object[]) builders), builder);
	}
	
	private static String createOrCondition(Object... objects) {
		return "(" 
			+ Arrays.stream(objects)
				.map(Object::toString)
				.collect(Collectors.joining("|"))
			+ ")";
	}

}
