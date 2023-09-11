package com.smarket.hdc2023.common;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class AES256 {
    private static final String TAG = "HWAES256";

    private static final String KEY = "key";
    private static final String IV = "iv";
    private static final String ALGORIGHM_NMAE = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";
    private static final String SALTED_PREFIX = "Salted__";
    private static final String DEFAULT_ENCODING = "UTF-8";

    static {

    }

    //生成随机数
    public static String randomivValue(int length) {
        try {
            StringBuffer num = new StringBuffer();
            SecureRandom rand = SecureRandom.getInstanceStrong();
            num.append(rand.nextInt(7));
            for (int i = 0; i < length - 1; i++) {
                num.append(rand.nextInt(10));
            }
            return num.toString();
        } catch (NoSuchAlgorithmException e) {
            return "12345";
        }
    }

    //生成key和iv
    public static Map<String, byte[]> generateKeyIV(String originalKey, String saltedValue) throws UnsupportedEncodingException {
        Map<String, byte[]> dataMap = new HashMap<>();

        //按照openssl规则生成key和iv
        String keyAndSalt = originalKey + saltedValue;

        byte[] secretKeyByte = keyAndSalt.getBytes(DEFAULT_ENCODING);

        byte[] hash1_128 = (MD5.toMd5(secretKeyByte));
        byte[] hash2_128 = new byte[secretKeyByte.length + hash1_128.length];

        System.arraycopy(hash1_128, 0, hash2_128, 0, hash1_128.length);
        System.arraycopy(secretKeyByte, 0, hash2_128, hash1_128.length, secretKeyByte.length);

        hash2_128 = MD5.toMd5(hash2_128);


        byte[] hash3_128 = new byte[secretKeyByte.length + hash2_128.length];
        System.arraycopy(hash2_128, 0, hash3_128, 0, hash2_128.length);
        System.arraycopy(secretKeyByte, 0, hash3_128, hash2_128.length, secretKeyByte.length);
        hash3_128 = MD5.toMd5(hash3_128);

        byte[] keyByte = new byte[hash1_128.length + hash2_128.length];
        System.arraycopy(hash1_128, 0, keyByte, 0, hash1_128.length);
        System.arraycopy(hash2_128, 0, keyByte, hash1_128.length, hash2_128.length);

        dataMap.put(KEY, keyByte);
        dataMap.put(IV, hash3_128);
        return dataMap;

    }

    //加密
    public static String aesEncrypt(String originalData, String originalKey) throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {

        String encrypedStr = "";

        if (originalData.length() < 1 || originalKey.length() < 1) {
            return null;
        }

        String saltedValue = randomivValue(8);

        //生成密钥和IV

        Map<String, byte[]> dataMap = null;
        try {
            dataMap = generateKeyIV(originalKey, saltedValue);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (null == dataMap) {
            return null;
        }

        byte[] skey = dataMap.get(KEY);
        byte[] siv = dataMap.get(IV);

        if (null == skey) {
            return null;
        }

        String skeyStr = new String(skey);
        String sivStr = new String(siv);

        SecretKeySpec keyObj = new SecretKeySpec(skey, AES);
        IvParameterSpec ivObj = new IvParameterSpec(siv);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORIGHM_NMAE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyObj, ivObj);


        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


        byte[] encryptedByte = cipher.doFinal(originalData.getBytes(DEFAULT_ENCODING));


        byte[] saltedByte = (SALTED_PREFIX + saltedValue).getBytes(DEFAULT_ENCODING);

        byte[] allEncryptedByte = new byte[encryptedByte.length + saltedByte.length];

        System.arraycopy(saltedByte, 0, allEncryptedByte, 0, saltedByte.length);
        System.arraycopy(encryptedByte, 0, allEncryptedByte, saltedByte.length, encryptedByte.length);

        //转base64
        encrypedStr = BASE64.encode(allEncryptedByte);

        return encrypedStr;
    }

    //解密
    public static String aesDecrypt(String originalData, String originalKey) throws Exception {
        String decryptedStr = null;


        try {
            //解密base64
            byte[] allEncryptedByte = BASE64.decode(originalData);

            byte[] saltedByte = new byte[8];

            System.arraycopy(allEncryptedByte, 8, saltedByte, 0, 8);
            String saltedvalue = new String(saltedByte);

            byte[] encryptedByte = new byte[allEncryptedByte.length - 16];

            System.arraycopy(allEncryptedByte, 16, encryptedByte, 0, encryptedByte.length);

            //生成密钥和IV
            Map<String, byte[]> dataMap = generateKeyIV(originalKey, saltedvalue);
            //datamap null

            byte[] skey = dataMap.get(KEY);
            byte[] siv = dataMap.get(IV);

            //解密
            SecretKeySpec key = new SecretKeySpec(skey, AES);
            IvParameterSpec iv = new IvParameterSpec(siv);

            Cipher cipher = Cipher.getInstance(ALGORIGHM_NMAE);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] originalByte = cipher.doFinal(encryptedByte);

            decryptedStr = new String(originalByte);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedStr;
    }

//    public static void HWAES256Test( )throws Exception
//    {
//
//        //明文
//
//        String content = "123456";
//
//
//        //密钥
//        String pkey = "003401015d8d698ce4b087efe0878df7";
//
//
//      //  if(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
//        {
//
//            MyLogger.D(TAG, "addProvider");
//            Security.addProvider(new BouncyCastleProvider());
//        }
//
//        MyLogger.D(TAG, "待加密报文" + content);
//
//        String aesEncryptStr = aesEncrypt(content, pkey);
//        MyLogger.D(TAG, "加密报文 " + aesEncryptStr);
//
//        String aesDecodeStr = aesDecrypt(aesEncryptStr, pkey);
//        MyLogger.D(TAG, "解密报文 " + aesDecodeStr);
//
//        boolean result = aesDecodeStr.equals(content);
//        MyLogger.D(TAG, "result = " + result);
//    }
}
