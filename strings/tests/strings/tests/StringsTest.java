package strings.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static strings.Strings.*;

import java.util.List;

import org.junit.Test;

public class StringsTest {

	@Test
	public void testRepeatString() {
		assertThat(repeat("st", 3))
			.isEqualTo("ststst");
		assertThat(repeat("a", 0))
			.isEqualTo("");
		assertThat(repeat("", 7))
			.isEqualTo("");
	}
	
	@Test
	public void testRepeatException() {
		assertThatThrownBy(() -> repeat("st", -1))
			.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void testRepeatChar() {
		assertThat(repeat('d', 3))
			.isEqualTo("ddd");
		assertThat(repeat('a', 1))
			.isEqualTo("a");
	}
	
	@Test
	public void testReplaceCharAt() {
		assertThat(replaceCharAt("strIng", 3, "a"))
			.isEqualTo("strang");
		
		assertThat(replaceCharAt("strIng", 3, 'a'))
			.isEqualTo("strang");
		
		assertThat(replaceCharAt("strIng", 3, ".."))
			.isEqualTo("str..ng");
		
		assertThat(replaceCharAt(" _ _ \n"
							   + "|_|_|", 3, 'a'))
			.isEqualTo(" _ a \n"
				     + "|_|_|");												
	}
	
	@Test
	public void testSubstringOccurences() {
		assertThat(substringOccurrences("hello world", "o"))
			.isEqualTo(new int[] {4, 7});
		
		assertThat(substringOccurrences("wonderful world", "wo"))
			.isEqualTo(new int[] {0, 10});
	}
	
	@Test
	public void testHighlightSubstringWithHighlighter() {
		assertThat(highlightSubstring("hello world", "o", s -> "[" + s + "]"))
			.isEqualTo("hell[o] w[o]rld");	
		
		assertThat(highlightSubstring("wonderful world", "wo", s -> s.toUpperCase()))
			.isEqualTo("WOnderful WOrld");
	}
	
	@Test
	public void testHighlightSubstring() {
		assertThat(highlightSubstring("hello world", "o"))
			.isEqualTo("hello world\n"
					 + "    ^  ^   ");	
		
		assertThat(highlightSubstring("wonderful world", "wo"))
			.isEqualTo("wonderful world\n"
				     + "^^        ^^   ");
	}
	
	@Test
	public void testHighlightSubstringInMultilineString() {
		assertThat(highlightSubstring("hello world\ndoes it", "o"))
			.isEqualTo("hello world\n"
					 + "    ^  ^   \n"
					 + "does it\n"
					 + " ^     ");	
	}
	
	@Test
	public void testSubstringsBetween() {
		assertThat(substringsBetween("{hello}", "{", "}"))
			.containsExactly("hello");
		
		assertThat(substringsBetween("n@gmail.it", "@", "."))
			.containsExactly("gmail");
		
		assertThat(substringsBetween("(a), (b)", "(", ")"))
			.containsExactly("a", "b");	
		
		assertThat(substringsBetween("(a), (b), (", "(", ")"))
			.containsExactly("a", "b");
		
		assertThat(substringsBetween("(a), (b), )", "(", ")"))
			.containsExactly("a", "b");
		
		assertThat(substringsBetween("num: 2008, num: 1,", "num: ", ","))
			.containsExactly("2008", "1");
	}
	
	@Test
	public void testSubstringBetween() {
		assertThat(substringBetween("{hello}", "{", "}"))
			.isEqualTo("hello");	
		
		assertThat(substringBetween("n@gmail.it", "@", "."))
			.isEqualTo("gmail");
		
		assertThat(substringBetween("(a), (b)", "(", ")"))
			.isEqualTo("a");	

		assertThat(substringBetween("date: 2008 may", "date: ", " may"))
			.isEqualTo("2008");
	}
	
	@Test
	public void testSideBySideWithOneString() {
		assertThat(sideBySide(5, "left"))
			.isEqualTo("left");	
	}
	
	@Test
	public void testSideBySideTwoStrings() {
		int whitespace = 5;
		String whitespaceString = whitespace(whitespace);
		
		assertThat(sideBySide(whitespace, "left", "right"))
			.isEqualTo("left" + whitespaceString + "right");	
		
		assertThat(sideBySide(whitespace, "left\nsecondline", "right\nsecondline"))
			.isEqualTo("left      " + whitespaceString + "right\n"
					 + "secondline" + whitespaceString + "secondline");
		
		assertThat(sideBySide(whitespace, "left\nsecondline\n3rd", "right\nsecondline\n3rd"))
			.isEqualTo("left      " + whitespaceString + "right\n"
					 + "secondline" + whitespaceString + "secondline\n"
					 + "3rd       " + whitespaceString + "3rd");
	}
	
	@Test
	public void testSideBySideMoreStrings() {
		int whitespace = 5;
		String whitespaceString = whitespace(whitespace);
		
		assertThat(sideBySide(whitespace, "left", "center", "right"))
			.isEqualTo("left" + whitespaceString + "center" + whitespaceString + "right");	
		
		assertThat(sideBySide(whitespace, "left\nsecondline", "center\nsecondline", "right\nsecondline"))
			.isEqualTo("left      " + whitespaceString + "center    " + whitespaceString + "right\n"
					 + "secondline" + whitespaceString + "secondline" + whitespaceString + "secondline");
		
		assertThat(sideBySide(whitespace, "left\nsecondline\n3rd", "center\nsecondline\n3rd", "right\nsecondline\n3rd"))
			.isEqualTo("left      " + whitespaceString + "center    " + whitespaceString + "right\n"
					 + "secondline" + whitespaceString + "secondline" + whitespaceString + "secondline\n"
					 + "3rd       " + whitespaceString + "3rd       " + whitespaceString + "3rd");
	}
	
	@Test
	public void testStringify() {
		assertThat(stringify(List.of(1, 2, 3)))
			.isEqualTo("[1, 2, 3]");
		
		assertThat(stringify(null))
			.isEqualTo("null");
	}
	
	@Test
	public void testToCharStream() {
		assertThat(toCharStream("hello"))
			.contains('h', 'e', 'l', 'l', 'o');
	}
	
	@Test
	public void testMapChars() {
		assertThat(mapChars("hello", ch -> ch != 'o' ? Character.toUpperCase(ch) : ch))
			.isEqualTo("HELLo");
		
		assertThat(mapChars("hello", ch -> ch + "."))
			.isEqualTo("h.e.l.l.o.");
		
		assertThat(mapChars("hello", ch -> ch + "\n ", true))
			.isEqualTo("hello\n     ");
	}
	
	@Test
	public void testCharMatches() {
		assertThat(charMatches("hello", 1, ch -> ch.equals('e')))
			.isTrue();
	}
	
	@Test
	public void testToCamelCase() {
		assertThat(toCamelCase(""))
			.isEqualTo("");
		
		assertThat(toCamelCase("string"))
			.isEqualTo("string");
		
		assertThat(toCamelCase("home sweet home"))
			.isEqualTo("homeSweetHome");
		
		assertThat(toCamelCase("home   sweet home"))
			.isEqualTo("homeSweetHome");
	}
	
	@Test
	public void testFromCamelCase() {
		assertThat(fromCamelCase("homeSweetHome"))
			.isEqualTo("home sweet home");
		
		assertThat(fromCamelCase("HomeSweetHome"))
			.isEqualTo("home sweet home");
	}
	
	@Test
	public void testSplitKeeping() {
		assertThat(splitKeeping("str", "", true))
			.containsExactly("s", "t", "r");
		
		assertThat(splitKeeping("a string", "\\d", true))
			.containsExactly("a string");
		
		assertThat(splitKeeping("homeSweetHome", "[A-Z]", true))
			.containsExactly("home", "Sweet", "Home");

		assertThat(splitKeeping("home Sweet Home", " ", false))
			.containsExactly("home ", "Sweet ", "Home");
	}
	
	@Test
	public void testLines() {
		assertThat(lines("home\n sweet home"))
			.containsExactly("home", " sweet home");
		
		assertThat(lines("home sweet \r\nhome"))
			.containsExactly("home sweet ", "home");
		
		assertThat(lines("home sweet\n\nhome"))
			.containsExactly("home sweet", "home");
	}
	
	@Test
	public void testWords() {
		assertThat(words("home sweet   home"))
			.containsExactly("home", "sweet", "home");
		
		assertThat(words("home\n sweet home"))
			.containsExactly("home", "sweet", "home");
	
		assertThat(words("home sweet \r\nhome"))
			.containsExactly("home", "sweet", "home");
		
		assertThat(words("via 0 19"))
			.containsExactly("via", "0", "19");
	}
	
	@Test
	public void testCapitalizeWords() {
		assertThat(capitalizeWords("home sweet home"))
			.isEqualTo("Home Sweet Home");
	}
	
	@Test
	public void testTrimChars() {
		assertThat(trimChars("hello", 1, 2))
			.isEqualTo("el");
		
		assertThat(trimChars("hello", 5, 2))
			.isEqualTo("");
		
		assertThatThrownBy(() -> trimChars("hello", 6, 0))
			.isInstanceOf(StringIndexOutOfBoundsException.class)
			.hasMessage("Cannot remove 6 characters from \"hello\"");
	}
	
	@Test
	public void testReverse() {
		assertThat(reverse("hello"))
			.isEqualTo("olleh");
	}
	
	@Test
	public void testEqualsOr() {
		assertThat(equalsAnyOf("a", "hello", "meow", "a"))
			.isTrue();
		
		assertThat(equalsAnyOf("a", "A"))
			.isFalse();
	}
	
	@Test
	public void testMatchedGroups() {
		assertThat(matchedGroups("ab", "(a)(b)"))
			.containsExactly("ab", "a", "b");
	}
	
	@Test
	public void testMismatch() {
		assertThat(mismatch("aaa", "aab"))
			.isEqualTo(2);
		
		assertThat(mismatch("aaa", "aaa789"))
			.isEqualTo(3);
	}
	
}
