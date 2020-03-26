package com.example.passenger.utils;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author suxijian
 * RSA加密工具类
 */
public class SoftSecurityUtil {
    //bouncy castle（轻量级密码术包）是一种用于 Java 平台的开放源码的轻量级密码术包BouncyCastleProvider()
    private static final Provider provider = new BouncyCastleProvider();

    private static final String charSet = "UTF-8";
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 生成密钥对并返回（公钥和私钥）
     */
    public static Map<String, String> createRSAKeys() throws Exception {
        //生成密钥对(公钥和私钥)
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
        kpg.initialize(1024, new SecureRandom());//seedKey.getBytes()
        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, String> RSAKeysMap = new HashMap<>();

        String publicRSAkeyBase64Str = encryptBase64(publicKey.getEncoded());
        String privateRSAkeyBase64Str = encryptBase64(privateKey.getEncoded());
        RSAKeysMap.put("publicRSAkeyBase64Str", publicRSAkeyBase64Str);
        RSAKeysMap.put("privateRSAkeyBase64Str", privateRSAkeyBase64Str);
        return RSAKeysMap;
    }

    /**
     * 加密
     *
     * @param data      明文
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String data, PublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", provider);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptResult = cipher.doFinal(data.getBytes(charSet));
        return encryptResult;
    }


    /**
     * 解密
     *
     * @param encryptedData 密文
     * @param privateKey    私钥
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", provider);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptResult = cipher.doFinal(encryptedData);
        return decryptResult;
    }


    /**
     * 获取公钥
     *
     * @param publicRSAkeyBase64Str base64编码后的String类型公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicRSAKey(String publicRSAkeyBase64Str) throws Exception {
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decryptBase64(publicRSAkeyBase64Str));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePublic(x509);
    }

    /**
     * 获取私钥
     *
     * @param privateRSAkeyBase64Str base64编码后的私钥
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateRSAKey(String privateRSAkeyBase64Str) throws Exception {
        PKCS8EncodedKeySpec pkgs8 = new PKCS8EncodedKeySpec(decryptBase64(privateRSAkeyBase64Str));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePrivate(pkgs8);
    }

    /**
     * base64编码
     * 将二进制数据编码成String类型
     */
    public static String encryptBase64(byte[] data) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(data);
    }

    /**
     * base64解码
     * 将经base64编码过的String类型数据解码成原来的二进制数据
     */
    public static byte[] decryptBase64(String data) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(data);
    }

}
