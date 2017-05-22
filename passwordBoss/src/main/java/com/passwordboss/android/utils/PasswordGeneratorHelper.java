package com.passwordboss.android.utils;

import java.security.SecureRandom;

public class PasswordGeneratorHelper {

	public static String generateDyn(int numDigits, boolean mNeedLetters, boolean mNeedCapitals, boolean mNeedDigits,
			boolean mNeedSpecialChars) {

		String genPass = "";
		String totalString = "";

		if (mNeedLetters) {
			String small = "abcdefghijklmnopqrstuvwxyz";
			SecureRandom rnd = new SecureRandom();
			int random = rnd.nextInt(small.length());
			genPass += "" + small.charAt(random);
			totalString += small;
		}

		if (mNeedCapitals) {
			String caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			SecureRandom rnd = new SecureRandom();
			int random = rnd.nextInt(caps.length());
			genPass += "" + caps.charAt(random);
			totalString += caps;
		}

		if (mNeedDigits) {
			String digits = "0123456789";
			SecureRandom rnd = new SecureRandom();
			int random = rnd.nextInt(digits.length());
			genPass += "" + digits.charAt(random);
			totalString += digits;
		}

		if (mNeedSpecialChars) {
			String special = "~!@#$%^&*()-_=+[{]}|;:<>/?";
			SecureRandom rnd = new SecureRandom();
			int random = rnd.nextInt(special.length());
			genPass += "" + special.charAt(random);
			totalString += special;
		}

		if (totalString.length() == 0)
			return null;

		int genPasslen=genPass.length();
		for (int i = 0; i < numDigits - genPasslen; i++) {
			SecureRandom rnd = new SecureRandom();
			int random = rnd.nextInt(totalString.length());
			genPass += "" + totalString.charAt(random);
		}

		return genPass;
	}
}
