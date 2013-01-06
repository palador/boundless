package org.pa.boundless.bsp;

public final class BspUtil {

	/**
	 * Converts a binary zero terminated char-array to a string.
	 * 
	 * @param chars
	 * @return
	 */
	public static String charsToString(char[] chars) {
		int lastBinZero = chars.length - 1;
		while (lastBinZero >= 0 && chars[lastBinZero] == 0) {
			lastBinZero--;
		}
		return new String(chars, 0, lastBinZero + 1);
	}

	private BspUtil() {
	}

}
