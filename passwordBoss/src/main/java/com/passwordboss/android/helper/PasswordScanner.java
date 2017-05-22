package com.passwordboss.android.helper;

import android.support.annotation.StringRes;

import com.passwordboss.android.R;

public class PasswordScanner {

    private final static int MIN_LENGTH = 8;
    private boolean mCapitals;
    private int mCapitalsCount;
    private boolean mDigits;
    private int mDigitsCount;
    private boolean mLetters;
    private int mLettersCount;
    private boolean mMinLength;
    private Strength mStrength;
    private boolean mSymbols;
    private int mSymbolsCount;

    private int calculatePasswordSum(int length, int uniqueLength) {
        if (uniqueLength <= 4) return 0;
        int result = uniqueLength * 4;
        if (hasCapitals()) result += (length - mCapitalsCount) * 2;
        if (hasLetters()) result += (length - mLettersCount) * 2;
        if (hasDigits()) result += mDigitsCount * 4;
        if (hasSymbols()) result += mSymbolsCount * 6;
        return result;
    }

    public void clearValues() {
        mLetters = false;
        mCapitals = false;
        mSymbols = false;
        mDigits = false;
        mMinLength = false;
        mStrength = Strength.Normal;
        mLettersCount = 0;
        mCapitalsCount = 0;
        mDigitsCount = 0;
        mSymbolsCount = 0;
    }

    private void determineStrength(int length, int uniqueLength) {
        final int sum = calculatePasswordSum(length, uniqueLength);
        if (sum < 16) {
            mStrength = Strength.Normal;
        } else if (sum < 31) {
            mStrength = Strength.Weak;
        } else if (sum < 56) {
            mStrength = Strength.Medium;
        } else if (sum < 81) {
            mStrength = Strength.Strong;
        } else {
            mStrength = Strength.VeryStrong;
        }
    }

    public Strength getStrength() {
        return mStrength;
    }

    public boolean hasCapitals() {
        return mCapitals;
    }

    public boolean hasDigits() {
        return mDigits;
    }

    public boolean hasLetters() {
        return mLetters;
    }

    public boolean hasMinLength() {
        return mMinLength;
    }

    public boolean hasSymbols() {
        return mSymbols;
    }

    public void scanPassword(String password) {
        password = password.trim();
        int length = password.length();
        clearValues();
        if (length >= MIN_LENGTH) mMinLength = true;
        if (length == 0) return;
        String unique = "";
        for (int i = 0; i < length; i++) {
            char c = password.charAt(i);
            if (unique.contains(String.valueOf(c))) continue;
            unique += c;
            if (c >= 'a' && c <= 'z') {
                mLettersCount++;
            } else if (c >= 'A' && c <= 'Z') {
                mCapitalsCount++;
            } else if (c >= '0' && c <= '9') {
                mDigitsCount++;
            } else {
                mSymbolsCount++;
            }
        }
        mLetters = mLettersCount > 0;
        mCapitals = mCapitalsCount > 0;
        mSymbols = mSymbolsCount > 0;
        mDigits = mDigitsCount > 0;
        determineStrength(length, unique.length());
    }

    public enum Strength {
        Normal(0, R.string.VeryWeak),
        Weak(25, R.string.Weak),
        Medium(50, R.string.Good),
        Strong(75, R.string.Strong),
        VeryStrong(100, R.string.VeryStrong);

        private final int mLength;
        private final int mStringId;

        Strength(int length, @StringRes int stringId) {
            mLength = length;
            mStringId = stringId;
        }


        public int getLength() {
            return mLength;
        }

        public int getStringId() {
            return mStringId;
        }

    }

}
