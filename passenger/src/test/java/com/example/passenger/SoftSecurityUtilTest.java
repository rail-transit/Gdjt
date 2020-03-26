package com.example.passenger;

import com.example.passenger.utils.MD5;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * @author suxijian
 * 测试RSA加密工具类
 * */
@SpringBootTest
public class SoftSecurityUtilTest {
    public static final Provider provider = new BouncyCastleProvider();

    private static final String charSet = "UTF-8";

    public static final String KEY_ALGORITHM = "RSA";

    @Test
    public void softSecurityUtilTests() throws Exception {

        //生成密钥对(公钥和私钥)
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
        kpg.initialize(1024, new SecureRandom());//seedKey.getBytes()
        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //公钥系数
        BigInteger publicKeyModulus = ((RSAPublicKey) keyPair.getPublic()).getModulus();
        //公钥指数
        BigInteger publicKeyExponent = ((RSAPublicKey) keyPair.getPublic()).getPublicExponent();

        //BigInteger与16进制之间的转化
        String s1 = "126656864e144ad88d7ff96badd2f68b"; // 16进制数
        BigInteger b = new BigInteger(s1,16);           // 16进制转成大数类型
        String s2 = b.toString(16);                     // 大数类型转成16进制

        String publicKeybaseStr = encryptBase64(publicKey.getEncoded());
        String privateKeybaseStr = encryptBase64(privateKey.getEncoded());

        //获取公私钥
        PublicKey publicKey1 = getPublicRSAKey(publicKeybaseStr);
        PrivateKey privateKey1 = getPrivateRSAKey(privateKeybaseStr);
        //加密
        String content = "jkdaldjfds加密成功2323";
        byte[] encryptRe =  encrypt(content, publicKey1);
        System.out.println("原文:"+content);
        System.out.println("加密文:"+encryptBase64(encryptRe));

        //解密
        byte[] decryptRe = decrypt(decryptBase64(encryptBase64(encryptRe)), privateKey1);
        String decryptData = new String(decryptRe,"utf-8");
        System.out.println("解密文:"+decryptData);
    }


    /**
     * 获取公钥
     * @param key base64加密后的公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicRSAKey(String key) throws Exception {
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decryptBase64(key));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePublic(x509);
    }

    /**
     * 获取私钥
     * @param key base64加密后的私钥
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateRSAKey(String key) throws Exception {
        PKCS8EncodedKeySpec pkgs8 = new PKCS8EncodedKeySpec(decryptBase64(key));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePrivate(pkgs8);
    }

    /**
     * 加密
     * @param input 明文
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String input, PublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", provider);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] re = cipher.doFinal(input.getBytes(charSet));
        return re;
    }

    /**
     * 解密
     * @param encrypted
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encrypted, PrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", provider);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] re = cipher.doFinal(encrypted);
        return re;
    }
    /**
     * base64解密
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * base64加密
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBase64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }


}
