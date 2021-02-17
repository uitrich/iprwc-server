package nl.iprwc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ArrayUtils {
    private ArrayUtils() { }

    public static byte[][] chunk(byte[] source, int chunkSize)
    {
        List<byte[]> chunks = new ArrayList<>();
        int start = 0;

        while (start < source.length) {
            int end = Math.min(source.length, start + chunkSize);
            chunks.add(Arrays.copyOfRange(source, start, end));
            start += chunkSize;
        }

        return chunks.toArray(new byte[0][]);
    }

    public static long[][] chunk(long[] source, int chunkSize)
    {
        List<long[]> chunks = new ArrayList<>();
        int start = 0;

        while (start < source.length) {
            int end = Math.min(source.length, start + chunkSize);
            chunks.add(Arrays.copyOfRange(source, start, end));
            start += chunkSize;
        }

        return chunks.toArray(new long[0][]);
    }

    public static <T> boolean contains(T[] arr, Object value)
    {
        for (Object item : arr) {
            if (item.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public static <T> boolean containsAny(T[] arr, Collection values)
    {
        for (Object value : values) {
            if (contains(arr, value)) {
                return true;
            }
        }

        return false;
    }

    public static <T> boolean containsAny(T[] arr, Object[] values)
    {
        for (Object value : Arrays.asList(values)) {
            if (contains(arr, value)) {
                return true;
            }
        }

        return false;
    }
}
