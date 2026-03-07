package com.blog.business.utils;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 浏览器指纹工具类
 * @author 31373
 */
public class FingerprintUtils {

    private static final String SECRET_KEY = "blog-web-key-admin@Jianyue.cloud";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final Logger log = LogManager.getLogger(FingerprintUtils.class);

    /**
     * 解密前端传递的指纹数据
     *
     * @param encryptedData 格式: "IV的Base64:密文的Base64"
     */
    public static String decrypt(String encryptedData) {
        try {
            if (encryptedData == null || !encryptedData.contains(":")) {
                throw new IllegalArgumentException("无效的加密数据格式");
            }
            //分割 IV 和 密文
            String[] parts = encryptedData.split(":");
            String ivString = parts[0];
            String cipherString = parts[1];

            //Base64 解码
            byte[] iv = Base64.getDecoder().decode(ivString);
            byte[] cipherText = Base64.getDecoder().decode(cipherString);
            byte[] key = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

            //配置解密参数
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            //解密
            byte[] original = cipher.doFinal(cipherText);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}