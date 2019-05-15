package crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

class SecureKey {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-=+[]{}/.?<>'";
    private static final String ALPHANUMERIC = UPPER + LOWER + DIGITS + SPECIAL;
    private final Random random;
    private final char[] symbols;
    private final char[] buf;
    private Logger logger = LoggerFactory.getLogger(Secret.class);

    private SecureKey(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an ALPHANUMERIC string generator.
     */
    private SecureKey(int length, Random random) {
        this(length, random, ALPHANUMERIC);
    }

    /**
     * Create an ALPHANUMERIC strings from a secure generator.
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
        String bufs = new String(buf);
        logger.info(bufs);
        return new String(buf);
    }

}