package com.atharva.auth.core.utils.encrypt;


import lombok.NonNull;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public abstract class EncryptUtils {

    private static final int SALT_IV_LENGTH_BIT = 128;
    private static final int SALT_IV_LENGTH_BYTE = 16;
    private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String CIPHER_TRANSFORM_ALGORITHM = "AES/GCM/NoPadding";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String PRIVATE_KEY = "x**yqF6%QwUA&Z?=SzU4?SVCFsjC^%w3*c6KE$y+LYh!m7Kr=MHJMhkC79en&Sdq";

    public String doUserEF(@NonNull String projectKey, @NonNull String hash2, @NonNull String data) {
        try {
            return doEF2(doEF1(projectKey, hash2, data));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException e) {
            throw new EncryptionException("Exception while encrypting UserData", e);
        }
    }

    public String undoUserEF(@NonNull String projectKey, @NonNull String hash2, @NonNull String data) {
        try {
            return undoEF1(projectKey, hash2, undoEF2(data));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException e) {
            throw new EncryptionException("Exception while decrypting UserData", e);
        }
    }

    public String doProjectEF(@NonNull String projectKey, @NonNull String data) {
        try {
            return doEF2(doEF(projectKey, data));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException e) {
            throw new EncryptionException("Exception while encrypting ProjectData", e);
        }
    }

    public String undoProjectEF(@NonNull String projectKey, @NonNull String data) {
        try {
            return undoEF(projectKey, undoEF2(data));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException e) {
            throw new EncryptionException("Exception while decrypting ProjectData", e);
        }
    }



    private static String xor(byte[] key1, byte[] key2) {
        int length = Math.max(key1.length, key2.length);
        byte[] key = new byte[length];

        for (int i=0 ; i<length ; i++) {
            if (i==key1.length) {
                key[i] = key2[i];
            } else if (i==key2.length) {
                key[i] = key1[i];
            } else {
                key[i] = (byte) (key1[i] ^ key2[i]);
            }
        }

        return Base64.getEncoder().encodeToString(key);
    }

    private static byte[] generateIv() {
        final SecureRandom random = new SecureRandom();
        final byte[] iv = new byte[SALT_IV_LENGTH_BYTE];
        random.nextBytes(iv);
        return iv;
    }

    private static SecretKeySpec generateSecretKeySpec(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        final KeySpec spec = new PBEKeySpec(key.toCharArray());
        final SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), ENCRYPTION_ALGORITHM);
    }



    private static String doEF(String key, String data) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException,
            InvalidKeyException {

        byte[] iv = generateIv();
        final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(SALT_IV_LENGTH_BIT, iv);

        final SecretKeySpec secretKeySpec = generateSecretKeySpec(key);

        final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORM_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);

        byte[] cipherText = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        byte[] cipherMessage = byteBuffer.array();

        return Base64.getEncoder().encodeToString(cipherMessage);
    }

    private String undoEF(String key, String cipherMessage) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(cipherMessage));
        byte[] iv = new byte[SALT_IV_LENGTH_BYTE];
        byteBuffer.get(iv);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(SALT_IV_LENGTH_BIT, iv);

        final SecretKeySpec secretKeySpec = generateSecretKeySpec(key);

        final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORM_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

        return new String(cipher.doFinal(cipherText));
    }



    private static String doEF1(String key1, String key2, String data) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        String key = xor(key1.getBytes(StandardCharsets.UTF_8), key2.getBytes(StandardCharsets.UTF_8));
        return doEF(key, data);
    }

    private static String doEF2(String data) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException,
            InvalidKeyException {

        return doEF(PRIVATE_KEY, data);
    }

    private String undoEF1(String key1, String key2, String cipherMessage) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        String key = xor(key1.getBytes(StandardCharsets.UTF_8), key2.getBytes(StandardCharsets.UTF_8));
        return undoEF(key, cipherMessage);
    }

    private String undoEF2(String cipherMessage) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        return undoEF(PRIVATE_KEY, cipherMessage);
    }

}
