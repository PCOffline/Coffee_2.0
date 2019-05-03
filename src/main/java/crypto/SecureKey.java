package crypto;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

class SecureKey {

    private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lower = upper.toLowerCase(Locale.ROOT);
    private static final String digits = "0123456789";
    private static final String special = "!@#$%^&*()-=+[]{}/.?<>'";
    private static final String alphanumeric = upper + lower + digits + special;
    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    private SecureKey(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    private SecureKey(int length, Random random) {
        this(length, random, alphanumeric);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    private SecureKey(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    SecureKey() {
        this(64);
    }

    /**
     * Generate a random string.
     */
    String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        System.out.println(new String(buf));
        return new String(buf);
    }

}