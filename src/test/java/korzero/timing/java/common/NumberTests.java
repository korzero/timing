package korzero.timing.java.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class NumberTests {
	@Test
	public void sumTest() {
		
		Number a = 3;
		Number b = new BigDecimal("1.2");
		
		BigDecimal bigA = new BigDecimal(a.toString());
		BigDecimal bigB = new BigDecimal(b.toString());
		
		
		assertThat(bigA.add(bigB), is(new BigDecimal("4.2")));
	}
}
