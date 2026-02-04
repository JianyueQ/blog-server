package com.blog.system.rabbitmqListener;

import com.blog.common.constant.CacheConstants;
import com.blog.common.core.redis.RedisCache;
import com.blog.system.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * 管理员详细信息监听类
 *
 * @author 31373
 */
@Component
public class AdminDetailedListener {

    private static final Logger log = LoggerFactory.getLogger(AdminDetailedListener.class);
    private final SysUserMapper sysUserMapper;
    private final RedisCache redisCache;


    public AdminDetailedListener(SysUserMapper sysUserMapper, RedisCache redisCache) {
        this.sysUserMapper = sysUserMapper;
        this.redisCache = redisCache;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("admin.details.queue"),
            exchange = @Exchange("admin.details.exchange"),
            key = "admin.logout"
    ))
    public void updateAdminDetailed(Map<String,Object> updateAdminDetailed) {
        try {
            String adminId = null;
            String loginTime = null;
            String ipaddr = null;
            if (updateAdminDetailed.containsKey("adminId")){
                adminId = updateAdminDetailed.get("adminId").toString();
            }
            if (updateAdminDetailed.containsKey("loginTime")){
                loginTime = (String) updateAdminDetailed.get("loginTime");
            }
            if (updateAdminDetailed.containsKey("ipaddr")){
                ipaddr = (String) updateAdminDetailed.get("ipaddr");
            }
            sysUserMapper.updateAdminDetailed(adminId, loginTime, ipaddr);
        } catch (Exception e) {
            log.error("更新管理员详细信息失败", e);
        }
    }

    private String getLoginUserInfoCacheKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

}
