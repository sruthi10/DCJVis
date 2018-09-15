package ca.corefacility.gview.utils;

import java.util.regex.Pattern;

/**
 */
public class Util
{
	// used soley to compare two doubles properly (taking into account rounding
	// errors).
	public static boolean isEqual(double d1, double d2)
	{
		return Math.abs(d1 - d2) < 0.000000001;
	}

	public static boolean isEqual(float f1, float f2)
	{
		return isEqual((double) f1, (double) f2); // is this proper?
	}

	public static boolean isEqual(Object p1, Object p2)
	{
		return (p1 == p2) || (p1 != null && p1.equals(p2));
	}

	/**
	 * Extracts the extension from the file name.
	 * 
	 * @param filename
	 *            The name of the file to extract the extension from.
	 * @return The extracted extension, or null if no extension.
	 */
	public static String extractExtension(String filename)
	{
		String extension = null;

		if (filename != null)
		{
			int periodIndex = filename.lastIndexOf('.');

			// if period is found, and it is not the last character
			if ((periodIndex != -1) && (periodIndex + 1) < filename.length())
			{
				extension = filename.substring(periodIndex + 1);
			}
		}

		return extension;
	}

	/**
	 * 
	 * @param s
	 * @return Whether or not the string is a double.
	 */
	public static boolean isDouble(String s)
	{
		final String Digits = "(\\p{Digit}+)";
		final String HexDigits = "(\\p{XDigit}+)";

		// an exponent is 'e' or 'E' followed by an optionally
		// signed decimal integer.
		final String Exp = "[eE][+-]?" + Digits;
		final String fpRegex = ("[\\x00-\\x20]*" + // Optional leading
													// "whitespace"
				"[+-]?(" + // Optional sign character
				"NaN|" + // "NaN" string
				"Infinity|" + // "Infinity" string

				// A decimal floating-point string representing a finite
				// positive
				// number without a leading sign has at most five basic pieces:
				// Digits . Digits ExponentPart FloatTypeSuffix
				//
				// Since this method allows integer-only strings as input
				// in addition to strings of floating-point literals, the
				// two sub-patterns below are simplifications of the grammar
				// productions from the Java Language Specification, 2nd
				// edition, section 3.10.2.

				// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
				"(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

		// . Digits ExponentPart_opt FloatTypeSuffix_opt
				"(\\.(" + Digits + ")(" + Exp + ")?)|" +

				// Hexadecimal strings
				"((" +
				// 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
				"(0[xX]" + HexDigits + "(\\.)?)|" +

				// 0[xX] HexDigits_opt . HexDigits BinaryExponent
				// FloatTypeSuffix_opt
				"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

				")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// Optional
																				// trailing
																				// "whitespace"

		if (Pattern.matches(fpRegex, s))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 
	 * @param s
	 * @return Whether or not the string is an integer.
	 */
	public static boolean isInteger(String s)
	{
		if (Pattern.compile("-?[0-9]+").matcher(s).matches())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
