package nl.iprwc.utils;

import java.security.SecureRandom;

public class Random {
    private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

    private static final SecureRandom random = new SecureRandom();

    public static SecureRandom getRandomInstance()
    {
        return random;
    }

    public static int randomIntInRange(int max)
    {
        return randomIntInRange(0, max);
    }

    public static int randomIntInRange(int min, int max)
    {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return random.nextInt((max - min) + 1) + min;
    }

    public static byte[] randomBytes(int length)
    {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String randomString(int length)
    {
        return randomString(length, CHAR_LIST);
    }

    public static String randomString(int length, String charList)
    {
        StringBuilder builder = new StringBuilder(length);
        int maxIndex = charList.length() - 1;

        for (int i = 0; i < length; i++) {
            int index = randomIntInRange(maxIndex);
            char chr = charList.charAt(index);
            builder.append(chr);
        }

        return builder.toString();
    }
}
