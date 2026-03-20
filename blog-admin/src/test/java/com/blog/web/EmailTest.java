package com.blog.web;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件发送测试类
 * 用于测试 Spring Boot 邮件配置是否生效
 *
 * @author 31373
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailTest {


    private static final Logger log = LoggerFactory.getLogger(EmailTest.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    /**
     * 测试简单邮件发送
     * 发送纯文本邮件
     */
    @Test
    public void testSendSimpleEmail() {
        if (mailSender == null) {
            log.error("JavaMailSender Bean 未找到，请检查邮件配置是否正确加载");
            throw new RuntimeException("JavaMailSender 未配置，无法发送邮件");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // 邮件发送方
            message.setFrom("3137390381@qq.com");
            // 邮件接收方 - 请替换为实际邮箱
            message.setTo("3137390381@qq.com");
            // 邮件主题
            message.setSubject("博客系统邮件测试 - 简单邮件");
            // 邮件内容
            message.setText("您好！\n\n这是一封测试邮件，用于验证博客系统的邮件发送功能是否正常。\n\n如果收到此邮件，说明邮件配置已成功生效！\n\n祝好，\n博客系统");

            mailSender.send(message);
            log.info("简单邮件发送成功！收件人：{}", message.getTo()[0]);
        } catch (Exception e) {
            log.error("简单邮件发送失败！", e);
            throw new RuntimeException("邮件发送失败：" + e.getMessage(), e);
        }
    }

}
