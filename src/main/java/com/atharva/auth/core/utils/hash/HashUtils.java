package com.atharva.auth.core.utils.hash;

import com.atharva.auth.core.model.HashModel;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class HashUtils {

    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;
    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String VERIFICATION_TEXT = "x**yqF6%QwUA&Z?=SzU4?SVCFsjC^%w3*c6KE$y+LYh!m7Kr=MHJMhkC79en&Sdq";

    private static byte[] generateSalt() {
        final SecureRandom random = new SecureRandom();
        final byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] generateHashUsingSalt(@NonNull final byte[] salt, @NonNull final String string)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        final KeySpec spec = new PBEKeySpec(string.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        final SecretKeyFactory factory = SecretKeyFactory.getInstance(HASH_ALGORITHM);

        return factory.generateSecret(spec).getEncoded();
    }

    public static HashModel getHashModel(@NonNull String id, @NonNull String encodedPass) {
        try {
            byte[] salt = generateSalt();
            byte[] hash1 = generateHashUsingSalt(salt, encodedPass);
            byte[] hash2 = generateHashUsingSalt(hash1, VERIFICATION_TEXT);
            return new HashModel(id, ArrayUtils.addAll(salt, hash2));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new HashException("Exception while hashing", e);
        }
    }

    public static boolean verifyHashModel(@NonNull HashModel hashModel, @NonNull String encodedPass) {
        try {
            byte[] hash1 = generateHashUsingSalt(ArrayUtils.subarray(hashModel.getPass(), 0, SALT_LENGTH), encodedPass);
            byte[] hash2 = generateHashUsingSalt(hash1, VERIFICATION_TEXT);
            return Arrays.equals(hash2, ArrayUtils.subarray(hashModel.getPass(), SALT_LENGTH, hashModel.getPass().length));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new HashException("Exception while hashing", e);
        }
    }
}
