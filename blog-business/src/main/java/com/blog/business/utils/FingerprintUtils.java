package com.blog.business.utils;

import com.alibaba.fastjson2.JSON;
import com.blog.business.domain.entity.VisitorRecordParameters;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.uuid.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /**
     * 生成唯一的哈希指纹,根据用户代理和客户端数据生成
     */
    public static String generateFingerprint(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String clientData = request.getHeader("Client-Data");
        String decrypt = decrypt(clientData);
        return generateFingerprint(userAgent, JSON.parseObject(decrypt, VisitorRecordParameters.class));
    }


    /**
     * 生成唯一的哈希指纹,根据用户代理和客户端数据生成
     *
     * @param userAgent 用户代理
     * @param clientData 客户端数据
     * @return 哈希指纹
     */
    public static String generateFingerprint(String userAgent, VisitorRecordParameters clientData) {
        try {
            // 收集用于生成指纹的信息
            StringBuilder fingerprintBuilder = new StringBuilder();
            if (StringUtils.isNotNull(clientData)) {
                //删除实体中的昵称,头像,邮箱
                clientData.setVisitor(null);
                String jsonString = JSON.toJSONString(clientData);
                fingerprintBuilder.append(jsonString);
            }
            // 用户代理字符串（浏览器特征）
            if (StringUtils.isNotEmpty(userAgent)) {
                fingerprintBuilder.append(userAgent);
            }
            // 生成MD5哈希作为指纹
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(fingerprintBuilder.toString().getBytes(StandardCharsets.UTF_8));

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("生成访客指纹时发生错误", e);
            // 备用方案：使用UUID
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

}