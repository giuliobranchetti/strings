package strings.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.regex.PatternSyntaxException;

import org.junit.Test;

import strings.regexp.RegExpBuilder;
import strings.regexp.escapechars.RegExpEscapeCharacters;

public class RegExpBuilderTest {
	
	@Test
	public void testStartEndGroup() {
		String regex = new RegExpBuilder()
				.startGroup()
				.match("a").once()
				.match("aa").zeroOrOneTimes()
				.endGroup()
				.build();
		
		assertThat(regex)
			.isEqualTo("(a(aa)?)");
	}
	
	@Test
	public void testStartGroupWithoutEndingIt() {
		assertThatThrownBy(() -> new RegExpBuilder()
				.startGroup()
				.match("a").oneOrMoreTimes()
				.build())
			.isInstanceOf(PatternSyntaxException.class)
			.hasMessageContaining("One or more groups were opened but never closed")
			.hasMessageContaining("(a+");
	}
	
	@Test
	public void testMatchString() {
		String regex = new RegExpBuilder()
				.match("a").once()
				.match("b").oneOrMoreTimes()
				.match("aa").once()
				.match("bb").zeroOrMoreTimes()
				.build();
		
		assertThat(regex)
			.isEqualTo("ab+aa(bb)*");
	}
	
	@Test
	public void testMatchSubBuilder() {
		RegExpBuilder subBuilder = new RegExpBuilder()
				.match("aa").once()
				.match(new RegExpBuilder()
						.match("bb").atLeastTimes(3)
					).once();
		
		String regex = new RegExpBuilder()
				.match(subBuilder).zeroOrOneTimes()
				.build();
		
		assertThat(regex)
			.isEqualTo("(aa(bb){3,})?");
	}
	
	@Test
	public void testMatchEscapeCharacter() {
		String regex = new RegExpBuilder()
				.match(RegExpEscapeCharacters.ALPHANUMERIC).once()
				.match(RegExpEscapeCharacters.TAB).times(2)
				.build();
		
		assertThat(regex)
			.isEqualTo("\\w\\t{2}");
	}
	
	@Test
	public void testMatchCharacterClassOfChars() {
		String regex = new RegExpBuilder()
				.matchAnyOf("abcd").once()
				.matchAnyOf("e(f)").zeroOrMoreTimes()
				.build();
		
		assertThat(regex)
			.isEqualTo("[abcd][e\\(f\\)]*"); 
	}
	
	@Test
	public void testMatchCharacterClassOfEscapeCharacters() {
		String regex = new RegExpBuilder()
				.matchAnyOf(
						RegExpEscapeCharacters.DIGIT, 
						RegExpEscapeCharacters.VERTICAL_TAB
					).once()
				.build();
		
		assertThat(regex)
			.isEqualTo("[\\d\\v]"); 
	}
	
	@Test
	public void testMatchCharacterClassWithRange() {
		String regex = new RegExpBuilder()
				.matchAnyOf(
						"ab",
						RegExpBuilder.range("A-Z")
					).once()
				.build();
		
		assertThat(regex)
			.isEqualTo("[abA-Z]"); 
		
		regex = new RegExpBuilder()
				.matchAnyOf(
						"ab",
						RegExpBuilder.range("K-f")
					).once()
				.build();
		
		assertThat(regex)
			.isEqualTo("[abK-f]"); 
		
		regex = new RegExpBuilder()
				.matchAnyOf(
						"ab",
						RegExpBuilder.range("d-f")
					).once()
				.build();
		
		assertThat(regex)
			.isEqualTo("[abd-f]"); 
		
		regex = new RegExpBuilder()
				.matchAnyOf(
						"ab",
						RegExpBuilder.range("1-9")
					).once()
				.build();
		
		assertThat(regex)
			.isEqualTo("[ab1-9]"); 
	}
	
	@Test
	public void testMatchCharacterClassWithMultipleRanges() {
		String regex = new RegExpBuilder()
				.matchAnyOf(
						"ab",
						RegExpBuilder.range("A-Za-z5-7")
					).once()
				.build();
		
		assertThat(regex)
			.isEqualTo("[abA-Za-z5-7]"); 
	}
	
	@Test
	public void testMatchCharacterClassWithIllegalRange() {
		assertThatThrownBy(() -> new RegExpBuilder()
				.matchAnyOf(RegExpBuilder.range("A")))
			.isInstanceOf(PatternSyntaxException.class)
			.hasMessageContaining("Illegal character class range");
		
		assertThatThrownBy(() -> new RegExpBuilder()
				.matchAnyOf(RegExpBuilder.range("a -z")))
			.isInstanceOf(PatternSyntaxException.class)
			.hasMessageContaining("Illegal character class range");
		
		assertThatThrownBy(() -> new RegExpBuilder()
				.matchAnyOf(RegExpBuilder.range("7-e")))
			.isInstanceOf(PatternSyntaxException.class)
			.hasMessageContaining("Illegal character class range");
		
		assertThatThrownBy(() -> new RegExpBuilder()
				.matchAnyOf(RegExpBuilder.range("v-H")))
			.isInstanceOf(PatternSyntaxException.class)
			.hasMessageContaining("Illegal character class range");

	}
	
	@Test
	public void testMatchCharacterClassWithOutOfOrderRange() {
		assertThatThrownBy(() -> new RegExpBuilder()
				.matchAnyOf(RegExpBuilder.range("Z-A")))
			.isInstanceOf(PatternSyntaxException.class)
			.hasMessageContaining("Out of order character class range");
		
		assertThatThrownBy(() -> new RegExpBuilder()
				.matchAnyOf(RegExpBuilder.range("v-b")))
			.isInstanceOf(PatternSyntaxException.class)
			.hasMessageContaining("Out of order character class range");
		
		assertThatThrownBy(() -> new RegExpBuilder()
				.matchAnyOf(RegExpBuilder.range("6-4")))
			.isInstanceOf(PatternSyntaxException.class)
			.hasMessageContaining("Out of order character class range");
	}
	
	@Test
	public void testMatchOrConditionOfStrings() {
		String regex = new RegExpBuilder()
				.matchAnyOf("a", "bb").once()
				.matchAnyOf("c", "d").zeroOrMoreTimes()
				.build();
		
		assertThat(regex)
			.isEqualTo("(a|bb)(c|d)*"); 
	}
	
//	@Test
//	public void testMatchOrConditionOfSubBuilders() {
//		RegExpBuilder subBuilder0 = new RegExpBuilder()
//				.matchAnyOf("123").zeroOrOneTimes();
//		
//		RegExpBuilder subBuilder1 = new RegExpBuilder()
//				.match("(a)").once();
//				
//		String regex = new RegExpBuilder()
//				.matchAnyOf(subBuilder0, subBuilder1).once()
//				.build();
//		
//		assertThat(regex)
//			.isEqualTo("([123]?|\\(a\\))"); 
//	}
	
	

}
