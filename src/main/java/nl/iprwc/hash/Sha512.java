package nl.iprwc.hash;

import nl.iprwc.utils.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Sha512 {
    private static final int CHUNK_SIZE = 64;
    private static final String HASH_NAME = "SHA-512";

    private final int chunkSize;

    public Sha512()
    {
        chunkSize = CHUNK_SIZE;
    }

    public Sha512(int inputChunkSize)
    {
        chunkSize = inputChunkSize;
    }

    public String hash(String input)
    {
        byte[] bytes = hashRaw(input.getBytes());
        return new String(Base64.getEncoder().encode(bytes));
    }

    public byte[] hashRaw(byte[] bytes)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        MessageDigest md;

        try {
            md = MessageDigest.getInstance(HASH_NAME);
        }
        catch (NoSuchAlgorithmException e) {
            return new byte[] { };
            // should never be thrown
        }

        byte[][] chunks = ArrayUtils.chunk(bytes, chunkSize);

        for (byte[] chunk : chunks) {
            try {
                byteStream.write(md.digest(chunk));
            }
            catch (IOException e) {
                return new byte[] { };
            }
        }

        return byteStream.toByteArray();
    }

    public boolean verifyHash(String input, String hash)
    {
        byte[] inputHash = hashRaw(input.getBytes());
        byte[] originalHash = Base64.getDecoder().decode(hash);
        return Arrays.equals(inputHash, originalHash);
    }
}
