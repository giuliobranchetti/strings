package strings.tests;

import static chars.Characters.decreaseLetter;
import static chars.Characters.getCorrespondingOnInvertingAlphabet;
import static chars.Characters.getIndexInAlphabet;
import static chars.Characters.increaseLetter;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharactersTest {

	@Test
	public void testGetIndexInAlphabet() {
		assertThat(getIndexInAlphabet('a'))
			.isEqualTo(0);
		assertThat(getIndexInAlphabet('E'))
			.isEqualTo(4);
		assertThat(getIndexInAlphabet('Z'))
			.isEqualTo(25);
	}
	
	@Test
	public void testIncreaseLetter() {
		assertThat(increaseLetter('b', 4))
			.isEqualTo('f');
		assertThat(increaseLetter('Z', 2))
			.isEqualTo('B');
		assertThat(increaseLetter('P', 26))
			.isEqualTo('P');
		assertThat(decreaseLetter('k', 1))
			.isEqualTo('j');
	}
	
	@Test
	public void testGetCorrespondingOnInvertingAlphabet() {
		assertThat(getCorrespondingOnInvertingAlphabet('X'))
			.isEqualTo('C');
		assertThat(getCorrespondingOnInvertingAlphabet('a'))
			.isEqualTo('z');
	}

}
