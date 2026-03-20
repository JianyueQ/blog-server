package com.blog.framework.mail;

import com.blog.framework.config.properties.EmailProperties;
import com.blog.system.service.ConfigService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 邮件服务类
 * 用于处理留言板、评论等相关邮件通知
 *
 * @author 31373
 */
@Service
public class EmailService {

    public static final String BLOG_NAME = "JianyueQ的个人站点";
    public static final String COMMENT_TYPE_TEXT = "comment";
    public static final String GUESTBOOK_TYPE_TEXT = "guestbook";
    public static final String EMAIL_CONFIG_KEY = "sys.email.enabled";
    public static final String EMAIL_TRUE = "true";
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    public EmailProperties emailProperties;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private ConfigService configService;

    /**
     * 发送评论/留言回复通知邮件
     *
     * @param toEmail        接收者邮箱
     * @param parentNickname 原评论/留言者昵称
     * @param parentContent  原评论/留言内容
     * @param replyNickname  回复者昵称
     * @param replyContent   回复内容
     * @param type           类型：comment-评论，message-留言
     */
    public void sendReplyNotification(String toEmail, String parentNickname, String parentContent,
                                      String replyNickname, String replyContent, String type) {
        //根据键名获取参数配置
        String emailEnabled = configService.selectConfigByKey(EMAIL_CONFIG_KEY);
        if (EMAIL_TRUE.equals(emailEnabled)) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(fromEmail);
                helper.setTo(toEmail);
                String typeText = COMMENT_TYPE_TEXT.equals(type) ? "文章评论" : "留言";
                helper.setSubject(BLOG_NAME + " - 您的" + typeText + "收到了新回复");
                helper.setText(buildReplyNotificationEmailContent(parentNickname, parentContent,
                        replyNickname, replyContent, typeText), true);
                mailSender.send(message);
                log.info("发送回复通知邮件成功：to={}, type={}", toEmail, type);
            } catch (Exception e) {
                log.error("发送回复通知邮件失败：to={}, type={}, ex={}", toEmail, type, e.getMessage());
            }
        }
    }

    /**
     * 构建回复通知邮件内容
     */
    private String buildReplyNotificationEmailContent(String parentNickname, String parentContent,
                                                      String replyNickname, String replyContent, String typeText) {
        String year = String.valueOf(LocalDate.now().getYear());
        return "<!DOCTYPE html>" +
                "<html lang='zh-CN'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>" + BLOG_NAME + " 回复通知</title>" +
                "    <style>" +
                "        body { margin: 0; padding: 0; font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif; background-color: #f5f5f5; }" +
                "        .email-container { max-width: 600px; margin: 40px auto; background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); overflow: hidden; }" +
                "        .email-header { background: #333; padding: 24px 0; text-align: center; border-bottom: 1px solid #222; }" +
                "        .email-content { padding: 36px 30px; }" +
                "        .quote-block { background: #f9f9f9; border-left: 4px solid #333; padding: 16px 20px; margin: 16px 0; border-radius: 0 8px 8px 0; }" +
                "        .reply-block { background: #f0f7ff; border-left: 4px solid #4a90d9; padding: 16px 20px; margin: 16px 0; border-radius: 0 8px 8px 0; }" +
                "        .footer { background: #222; padding: 16px; text-align: center; color: #aaa; font-size: 12px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='email-container'>" +
                "        <div class='email-header'>" +
                "            <h1 style='color: white; margin: 0; font-size: 24px; font-weight: 500;'>" + BLOG_NAME + "</h1>" +
                "            <p style='color: #bbb; margin: 8px 0 0; font-size: 14px;'>—— " + typeText + "回复通知 ——</p>" +
                "        </div>" +
                "        <div class='email-content'>" +
                "            <h2 style='color: #333; margin: 0 0 20px; font-size: 20px; font-weight: 500;'>" + parentNickname + "，您好！</h2>" +
                "            <p style='color: #555; font-size: 15px; line-height: 1.6; margin: 0 0 16px;'>您的" + typeText + "收到了来自 <strong>" + replyNickname + "</strong> 的回复：</p>" +
                "            <div class='quote-block'>" +
                "                <p style='color: #888; margin: 0 0 6px; font-size: 13px;'>您的原始内容：</p>" +
                "                <p style='color: #555; margin: 0; font-size: 14px; line-height: 1.6;'>" + parentContent + "</p>" +
                "            </div>" +
                "            <div class='reply-block'>" +
                "                <p style='color: #666; margin: 0 0 6px; font-size: 13px;'>" + replyNickname + " 的回复：</p>" +
                "                <p style='color: #333; margin: 0; font-size: 14px; line-height: 1.6;'>" + replyContent + "</p>" +
                "            </div>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p style='margin: 0; line-height: 1.5;'>此为系统自动发送的邮件，请勿直接回复<br> " + year + " " + BLOG_NAME + " - 保留所有权利</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

}
