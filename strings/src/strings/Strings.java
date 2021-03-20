package strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Strings {

	public static String indentTabs(int tabs) {
		return repeat("\t", tabs);
	}

	public static String indentSpaces(int doubleSpaces) {
		return repeat("  ", doubleSpaces);
	}
	
	/**
	 * 
	 * @param str
	 * @return an array of lines extracted from {@code str} 
	 */
	public static String[] lines(String str) {
		return str.split("(\r?\n)+");
	}
	
	/**
	 * 
	 * @param str
	 * @return an array of words extracted from {@code str} 
	 */
	public static String[] words(String str) {
		return str.split("[ (\r?\n)]+");
	}

	/**
	 * Converts the first letter of {@code str} to upper case.
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * Converts the first letter of every word in {@code str} to upper case.
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalizeWords(String str) {
		return Arrays.stream(words(str))
				.map(Strings::capitalize)
				.collect(Collectors.joining(" "));
	}
	
	public static String prepend(String str, String prefix) {
		return prefix + str;
	}
	
	public static String append(String str, String suffix) {
		return str + suffix;
	}

	/**
	 * 
	 * @param str
	 * @return the string in quotes
	 */
	public static String inQuotes(String str) {
		return "\"" + str + "\"";
	}
	
	public static String replaceCharAt(String str, int index, String replacement) {
		return str.substring(0, index) + replacement 
			+ str.substring(index + 1); 
	}
	
	public static String replaceCharAt(String str, int index, char replacement) {
		return replaceCharAt(str, index, Character.toString(replacement));
	}
	
	/**
	 * Removes {@code leading} characters from the beginning of {@code str}
	 * and {@code trailing} characters from its ending.
	 * 
	 * @param str
	 * @param leading
	 * @param trailing
	 * @return
	 * @exception StringIndexOutOfBoundsException if {@code leading} or
	 * 		      {@code trailing} are bigger than {@code str.length()}
	 */
	public static String trimChars(String str, int leading, int trailing) {
		checkLength(str, leading);
		checkLength(str, trailing);
		
		if (leading + trailing > str.length())
			return "";
		
		return str.substring(leading, str.length() - trailing);
	}
	
	private static void checkLength(String str, int n) {
		if (n > str.length())
			throw new StringIndexOutOfBoundsException("Cannot remove " + n + " characters from " 
					+ inQuotes(str));
	}
	
	/**
	 * @param str
	 * @param n
	 * @return 
	 * @exception StringIndexOutOfBoundsException if {@code n} is bigger 
	 *            than {@code str.length()}
	 */
	public static String removeLastNChars(String str, int n) {
		return trimChars(str, 0, n);
	}
	
	/**
	 * @param str
	 * @return
	 * @exception StringIndexOutOfBoundsException if {@code str.length()} is
	 *            less than 1
	 */
	public static String removeLastChar(String str) {
		return removeLastNChars(str, 1);
	}
	
	/**
	 * 
	 * @param str
	 * @param n
	 * @return
	 * @exception StringIndexOutOfBoundsException if {@code n} is bigger 
	 *            than {@code str.length()}
	 */
	public static String removeFirstNChars(String str, int n) {
		return trimChars(str, n, 0);
	}

	/**
	 * @param str
	 * @param times
	 * @return a string consisting of {@code str} repeated {@code times} times 
	 * @exception IllegalArgumentException if {@code times} is negative
	 */
	public static String repeat(String str, int times) {
		if (times < 0)
			throw new IllegalArgumentException("Cannot repeat string a negative amount of times");

		String result = "";
		for (int i = 0; i < times; i++)
			result += str;
		return result;
	}

	/**
	 * @param ch
	 * @param times
	 * @return a string consisting of {@code ch} repeated {@code times} times 
	 * @exception IllegalArgumentException if {@code times} is negative
	 */
	public static String repeat(char ch, int times) {
		return repeat(Character.toString(ch), times);
	}

	/**
	 * @param length
	 * @return a string of whitespace with length {@code length}
	 */
	public static String whitespace(int length) {
		return repeat(" ", length);
	}

	/**
	 * @param str
	 * @param substr
	 * @return an array of indexes where each index represents the position of the first
	 *         letter of the found occurrence of {@code substr} in {@code str}
	 * @throws NonExistentSubstringException if {@code substr} isn't contained in {@code str} 
	 */
	public static int[] substringOccurrences(String str, String substr) {
		checkSubstringContained(str, substr);
		
		List<Integer> occurrences = new ArrayList<>();
		int fromIndex = 0;
		int substrIndex;
		
		while (true) {
			substrIndex = str.indexOf(substr, fromIndex);
			if (substrIndex == -1) 
				break;
			
			fromIndex = substrIndex + substr.length();
			
			occurrences.add(substrIndex);
		} 
		
		int size = occurrences.size();
		int[] toReturn = new int[size];
		for(int i = 0; i < size; i++) 
			toReturn[i] = occurrences.get(i);
		return toReturn;
	}
	
	/**
	 * Appends a line to {@code str} to highlight {@code substr}.
	 * <blockquote>For example,<pre>
     * {@code highlightSubstring("house mouse", "us") 
     * => "house mouse"
     *    "  ^^    ^^ "
     * }</pre></blockquote>
     * 
	 * @param str
	 * @param substr
	 * @param highlighter the function that will be applyed to every matching character 
	 * @throws NonExistentSubstringException if {@code substr} isn't contained in {@code str} 
	 */
	public static String highlightSubstring(String str, String substr, Function<String, Object> highlighter) {
		checkSubstringContained(str, substr);

		int substrLength = substr.length();		
		String result = "";
		
		for (String line : lines(str)) {
			int prevOccurence = 0;
			for (int occurence : substringOccurrences(line, substr)) {
				result += str.substring(prevOccurence, occurence) // parte non di substr
				        + highlighter.apply(str.substring(occurence, 
				        		prevOccurence = occurence + substrLength));
			}
			
			result += str.substring(prevOccurence); // parte dopo ultima substr
		}
		
		return result;
	}
	
	/**
	 * Appends a line to {@code str} to highlight {@code substr}.
	 * <blockquote>For example,<pre>
     * {@code highlightSubstring("house mouse", "us") 
     * => "house mouse"
     *    "  ^^    ^^ "
     * }</pre></blockquote>
     * 
	 * @param str
	 * @param substr
	 * @throws NonExistentSubstringException if {@code substr} isn't
	 *            contained in {@code str} 
	 */
	public static String highlightSubstring(String str, String substr) {
		checkSubstringContained(str, substr);

		int substrLength = substr.length();		
		String toReturn = "";
		
		for (String line : lines(str)) {
			String arrowsLine = "";
			int prevOccurence = 0;
			for (int occurence : substringOccurrences(line, substr)) {
				arrowsLine += whitespace(occurence - prevOccurence)
							+ repeat("^", substrLength);
				prevOccurence = occurence + substrLength; 
			}
			arrowsLine += whitespace(line.length() - prevOccurence);
			
			toReturn += line + "\n" + arrowsLine + "\n";
		}
		
		return removeLastChar(toReturn);
	}
	
	/** 
	 * @param str
	 * @param start
	 * @param end
	 * @return a list of all substrings of {@code str} found between a substring {@code start}
	 *         and a substring {@code end}
	 * @exception NonExistentSubstringException if {@code start} or {@code end} aren't
	 *            contained in {@code str} 
	 */
	public static List<String> substringsBetween(String str, String start, String end) {
		checkSubstringContained(str, start);
		checkSubstringContained(str, end);
		
		int startLength = start.length();
		int endLength = end.length();
		
		List<String> substrings = new ArrayList<>();
		int fromIndex = 0;
		
		while (true) {
			int startIndex = str.indexOf(start, fromIndex);
			int endIndex = str.indexOf(end, fromIndex);
			if (startIndex == -1 || endIndex == -1) 
				break;

			fromIndex = endIndex + endLength;
			
			String target = str.substring(startIndex + startLength, endIndex);
			substrings.add(target);
		} 

		return substrings;
	}
	
	/**
	 * {@code start} and {@code end} are excluded in the result.
	 * <blockquote>For example:<pre>
	 * {@code substringBetween("hello", "he", "lo") = "l".}
	 * </pre></blockquote>
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return the substring between the substrings {@code start} and {@code end}
	 * @exception NonExistentSubstringException if {@code start} or {@code end} aren't
	 *            contained in {@code str} 
	 */
	public static String substringBetween(String str, String start, String end) {
//		boolean startNotContained = !str.contains(start);
//		boolean endNotContained = !str.contains(end);
//		if (startNotContained && endNotContained)
//			return str;
//		if (startNotContained)
//			return substringBefore(str, end);
//		if (endNotContained)
//			return substringAfter(str, start);
		checkSubstringContained(str, start);
		checkSubstringContained(str, end);

		return str.substring(str.indexOf(start) + start.length(), 
					         str.indexOf(end));
	}
	
	/**
	 * {@code start} is excluded in the result.
	 * 
	 * @param str
	 * @param start
	 * @return the substring between the first occurrence of the substring {@code start} 
	 *         and the end of the string
	 * @exception NonExistentSubstringException if {@code start} isn't contained in {@code str} 
	 */
	public static String substringAfter(String str, String start) {
		checkSubstringContained(str, start);

		return str.substring(str.indexOf(start) + start.length());
	}
	
	/**
	 * {@code end} is excluded in the result.
	 * 
	 * @param str
	 * @param end
	 * @return the substring between the start of the string and the first
	 *         occurrence of substring {@code end} 
	 * @exception NonExistentSubstringException if {@code end} isn't contained in {@code str} 
	 */
	public static String substringBefore(String str, String end) {
		checkSubstringContained(str, end);

		return str.substring(0, str.indexOf(end));
	}

	private static void checkSubstringContained(String str, String substr) {
		if (!str.contains(substr))
			throw new NonExistentSubstringException("Couldn't find substring " 
					+ inQuotes(substr) + " in " + inQuotes(str));
	}
	
	private static String sideBySideTwoStrings(String left, String right, int whitespaceBetween) {
		List<String> leftLines = left.lines()
				.collect(Collectors.toList());
		List<String> rightLines = right.lines()
				.collect(Collectors.toList());
		
		int leftWidth = leftLines.stream()
				.mapToInt(String::length)
				.max()
				.orElse(0);
		
		int leftHeight = leftLines.size();
		int rightHeight = rightLines.size();
		int maxHeight = leftHeight > rightHeight ? leftHeight : rightHeight;
		
		String whitespaceBetweenString = whitespace(whitespaceBetween);
		String result = "";
		for (int i = 0; i < maxHeight; i++) {
			String leftLine = i < leftHeight ? leftLines.get(i) : "";
			String rightLine = i < rightHeight ? rightLines.get(i) : "";
			
			result += leftLine + whitespace(leftWidth - leftLine.length())
					+ whitespaceBetweenString + rightLine + "\n";
		}

		return removeLastChar(result);
	}
	
	/**
	 * @param whitespaceBetween
	 * 		  the number of blank spaces between each column
	 * @param columns
	 * @return a string representing all the strings passed as an argument side by side
	 */
	public static String sideBySide(int whitespaceBetween, String... columns) {
		if (columns == null || columns.length == 0)
			return "";

		return Arrays.stream(columns)
				.skip(1)
				.reduce(columns[0], (result, column) -> 
					result = sideBySideTwoStrings(result, column, whitespaceBetween));
	}
	
	/**
	 * @param whitespaceBetween
	 * 		  the number of blank spaces between each column
	 * @param columns
	 * 		  a collection containing the strings
	 * @return a string representing all the strings in the collection passed as an argument side by side
	 */
	public static String sideBySide(int whitespaceBetween, Collection<String> columns) {
		return sideBySide(whitespaceBetween, columns.toArray(String[]::new));
	}
	
	/**
	 * Prints any number of strings using the method {@link #sideBySide(whitespaceBetween, columns)}.
	 * 
	 * @param whitespaceBetween
	 * 		  the number of blank spaces between each column
	 * @param columns
	 */
	public static void printSideBySide(int whitespaceBetween, String... columns) {
		System.out.println(sideBySide(whitespaceBetween, columns));
	}
	
	/**
	 * Prints a collection of strings using the method {@link #sideBySide(int whitespaceBetween, String... columns)}.
	 * 
	 * @param whitespaceBetween
	 * 		  the number of blank spaces between each column
	 * @param columns
	 * 		  a collection containing the strings
	 */
	public static void printSideBySide(int whitespaceBetween, Collection<String> columns) {
		System.out.println(sideBySide(whitespaceBetween, columns));
	}
	
	/**
	 * Returns a string representation of the Object using the method {@link #toString()}.
	 * In case of {@code null}, it returns the string "null".
	 * 
	 * @param obj
	 * @return a string representing {@code obj}
	 */
	public static String stringify(Object obj) {
		return obj == null ? "null" : obj.toString(); 
	}
	
	/**
	 * @param str
	 * @return a stream of the characters of {@code str}
	 */
	public static Stream<Character> toCharStream(String str) {
		Collection<Character> chars = new ArrayList<>();
		for (char ch : str.toCharArray())
			chars.add(ch);
		
		return chars.stream();
	}

	/**
	 * Maps every character of {@code str} to a new substring based on {@code mapper}.
	 * 
	 * @param str
	 * @param mapper
	 *        the function that will be applied to every character
	 * @param sideBySide 
	 *        if {@code true} the mapped substrings are joined side by side, calling the method
	 *        {@link #sideBySide(zero, substrings)}
	 * @return a new string with modified characters
	 */
	public static String mapChars(String str, Function<Character, Object> mapper, boolean sideBySide) {
		Stream<String> mapped = toCharStream(str)
			.map(mapper)
			.map(Object::toString);
		
		if (sideBySide)
			return mapped.reduce("", (result, chunk) -> sideBySide(0, result, chunk));
		return mapped.collect(Collectors.joining());
	}
	
	/**
	 * Maps every character of {@code str} to a new substring based on {@code mapper}
	 * calling the method {@link #mapChars(str, mapper, false)}.
	 * 
	 * @param str
	 * @param mapper 
	 *        the function that will be applied to every character
	 * @return a new string with modified characters
	 */
	public static String mapChars(String str, Function<Character, Object> mapper) {
		return mapChars(str, mapper, false);
	}
	
	/**
	 * Consumes every character of the string {@code str}.
	 * 
	 * @param str
	 * @param operation
	 */
	public static void forEachCharDo(String str, Consumer<Character> operation) {
		toCharStream(str)
			.forEach(operation);
	}
	
	/**
	 * Tests if the character at index {@code charIndex} in the string {@code str}
	 * matches the predicate.
	 * 
	 * @param str
	 * @param charIndex
	 * @param predicate
	 * @return {@code true} if the character matches, {@code false} otherwise
	 * @throws IndexOutOfBoundsException if {@code charIndex} is out of bounds
	 */
	public static boolean charMatches(String str, int charIndex, Predicate<Character> predicate) {
		if (charIndex < 0 || charIndex >= str.length())
			throw new IndexOutOfBoundsException(charIndex + " is not a valid char index for string " + inQuotes(str));
		
		return predicate.test(str.charAt(charIndex));
	}
	
	/**
	 * Tests if the character at index 0 in the string {@code str}
	 * matches the predicate, using the method {@link #charMatches(str, index0, predicate)}.
	 * 
	 * @param str
	 * @param predicate
	 * @return {@code true} if the string starts with a matching character, {@code false} otherwise
	 */
	public static boolean startsWith(String str, Predicate<Character> predicate) {
		return charMatches(str, 0, predicate);
	}
	
	/**
	 * Tests if the character at index {@code str.length()-1} in the string {@code str}
	 * matches the predicate, using the method {@link #charMatches(str, str.length, predicate)}.
	 * 
	 * @param str
	 * @param predicate
	 * @return {@code true} if the string ends with a matching character, {@code false} otherwise
	 */
	public static boolean endsWith(String str, Predicate<Character> predicate) {
		return charMatches(str, str.length()-1, predicate);
	}
	
	/**
	 * 
	 * @param str
	 * @param ch
	 * @return {@code true} if {@code str} starts with the character {@code ch}, 
	 * 	       {@code false} otherwise
	 */
	public static boolean startsWith(String str, char ch) {
		return str.startsWith(Character.toString(ch));
	}
	
	/**
	 * 
	 * @param str
	 * @param ch
	 * @return {@code true} if {@code str} ends with the character {@code ch},
	 * 	       {@code false} otherwise
	 */
	public static boolean endsWith(String str, char ch) {
		return str.endsWith(Character.toString(ch));
	}
	
	/**
	 * Makes sure {@code str} ends with the character {@code ch}. If it doesn't, 
	 * appends {@code ch} to the end of {@code str}.
	 * 
	 * @param str
	 * @param ch
	 */
	public static String requireEndingWith(String str, char ch) {
		return endsWith(str, ch) ? str : (str + ch);
	}
	
	/**
	 * Makes sure {@code str} ends with the string {@code suffix}.
	 * If it doesn't, appends {@code suffix} to the end of {@code str}.
	 * 
	 * @param str
	 * @param suffix
	 */
	public static String requireEndingWith(String str, String suffix) {
		return str.endsWith(suffix) ? str : (str + suffix);
	}
	
	/**
	 * Makes sure {@code str} starts with the character {@code ch}.
	 * If it doesn't, adds {@code ch} to the start of {@code str}.
	 * 
	 * @param str
	 * @param ch
	 */
	public static String requireStartingWith(String str, char ch) {
		return startsWith(str, ch) ? str : (ch + str);
	}
	
	/**
	 * Makes sure {@code str} starts with the string {@code prefix}.
	 * If it doesn't, adds {@code prefix} to the start of {@code str}.
	 * 
	 * @param str
	 * @param prefix
	 */
	public static String requireStartingWith(String str, String prefix) {
		return str.startsWith(prefix) ? str : (prefix + str);
	}
	
	/**
	 * Converts a string to camel case.
	 * 
	 * @param str
	 * @return
	 */
	public static String toCamelCase(String str) {
		String[] words = words(str);
		String camelCase = "";
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (i > 0)
				word = capitalize(word);
			camelCase += word;
		}
		return camelCase;
	}
	
	/**
	 * Converts a camel case string to normal syntax.
	 * 
	 * @param camelCase
	 * @return a string where all words are lower case
	 */
	public static String fromCamelCase(String camelCase) {
		String[] words = splitKeeping(camelCase, "[A-Z]", true);
		return Arrays.stream(words)
				.filter(word -> !word.isBlank())
				.map(String::toLowerCase)
				.collect(Collectors.joining(" "));
	}
	
	/**
	 * Performs the operation {@code str.split(regex)} redefined so that
	 * it doesn't remove the characters matching {@code regex}.
	 * 
	 * @param str
	 * @param regex
	 * @param matchPosition 
	 * 		  if {@code false} the characters matching {@code regex} are appended to 
	 *        the preceding substring, otherwise to the suceeding substring
	 * @return an array of strings 
	 * @throws PatternSyntaxException if the regex's syntax is invalid
	 */
	public static String[] splitKeeping(String str, String regex, boolean matchPosition) {
		String[] splits = str.split(regex); 
		String[] result = Arrays.copyOf(splits, splits.length);
		String[] matches = matchedResults(str, regex);
		
		if (matchPosition) {
			for (int i = 1; i < result.length; i++) 
				result[i] = matches[i-1] + result[i];
		}
		else {
			for (int i = 0; i < result.length-1; i++) 
				result[i] =  result[i] + matches[i];
		}

		return result;
	}
	
	/**
	 * Reverses {@code str}.
	 * 
	 * @param str
	 * @return a new string
	 */
	public static String reverse(String str) {
		return new StringBuilder(str)
				.reverse()
				.toString();
	}
	
	/**
	 * @param str
	 * @param objects
	 * @return wheter {@code str} equals at least one object from {@code objects}
	 */
	public static boolean equalsAnyOf(String str, Object... objects) {
		return Arrays.stream(objects)
				.anyMatch(obj -> str.equals(obj));
	}
	
	/**
	 * @param str
	 * @param regex
	 * @return an array of strings containing the matches of {@code regex} found
	 * 	       in the given string
	 * @throws PatternSyntaxException if the regex's syntax is invalid
	 */
	public static String[] matchedResults(String str, String regex) {
		return Pattern.compile(regex)
			.matcher(str)
			.results()
			.map(MatchResult::group)
			.toArray(String[]::new);
	}

	/**
	 * The index in the returned array corresponds to the index that would be used in the method 
	 * {@code Matcher.group(index)}. This means that index 0 contains {@code str}.
	 * 
	 * @param str
	 * @param regex
	 * @return an array of strings containing the groups matched
	 * @throws PatternSyntaxException if the regex's syntax is invalid
	 * @throws IllegalArgumentException if the regex doesn't match {@code str}
	 */
	public static String[] matchedGroups(String str, String regex) {
		Matcher matcher = Pattern.compile(regex)
				.matcher(str);
		
		if (!matcher.matches())
			throw new IllegalArgumentException(inQuotes(regex) + " doesn't match " + inQuotes(str));

		int groupCount = matcher.groupCount();
		String[] groups = new String[groupCount + 1];
		groups[0] = str;
		for (int i = 1; i < groupCount + 1; i++)
			groups[i] = matcher.group(i);

		return groups;
	}
	
	/**
     * Simply calls {@link Arrays}{@code .mismatch(str.toCharArray(), other.toCharArray())}.
	 * @param str
	 * @param other
	 * @return the index of the first mismatch between {@code str} and {@code other}. 
	 * 		   -1 if no mismatch is found.
	 */
	public static int mismatch(String str, String other) {
		return Arrays.mismatch(str.toCharArray(), other.toCharArray());
	}

}
