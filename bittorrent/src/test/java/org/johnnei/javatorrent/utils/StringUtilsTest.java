package org.johnnei.javatorrent.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link StringUtils}
 */
public class StringUtilsTest {

	@Test
	public void testByteArrayToString() throws Exception {
		byte[] input = new byte[] { 0xF, 0x12, (byte) 0xF0 };
		String expectedOutput = "0F12F0";

		assertEquals(expectedOutput, StringUtils.byteArrayToString(input));
	}
}
