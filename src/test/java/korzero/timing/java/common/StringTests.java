package korzero.timing.java.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringTests {

	@Test
	public void testSubString() {
		String text = "AABBCC가나다";
		
		assertThat(text.substring(0, 2), is("AA"));
		assertThat(text.substring(2, 4), is("BB"));
		assertThat(text.substring(4, 6), is("CC"));
		assertThat(text.substring(6, 8), is("가나"));
		
	}
}
