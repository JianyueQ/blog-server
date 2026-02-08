package com.blog.system.consumers;

import com.alibaba.fastjson2.JSON;
import com.blog.common.constant.Constants;
import com.blog.common.constant.RabbitMqConstants;
import com.blog.common.core.domain.entity.SysLogininfor;
import com.blog.common.core.domain.model.LoginLog;
import com.blog.common.utils.LogUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.http.UserAgentUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.spring.SpringUtils;
import com.blog.system.service.AccessRecordsService;
import com.blog.system.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 管理员详细信息监听类
 *
 * @author 31373
 */
@Component
public class AdminDetailedConsumers {

    private static final Logger log = LoggerFactory.getLogger(AdminDetailedConsumers.class);

    /**
     * 更新管理员登录信息
     *
     * @param updateAdminDetailed 更新管理员登录信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(RabbitMqConstants.ADMIN_LOGIN_QUEUE),
            exchange = @Exchange(RabbitMqConstants.ADMIN_DETAIL_EXCHANGE),
            key = RabbitMqConstants.ADMIN_LOGIN_KEY
    ))
    public void updateAdminDetailed(Map<String, Object> updateAdminDetailed) {
        try {
            String adminId = null;
            String loginTime = null;
            String ipaddr = null;
            if (updateAdminDetailed.containsKey("adminId")) {
                adminId = updateAdminDetailed.get("adminId").toString();
            }
            if (updateAdminDetailed.containsKey("loginTime")) {
                loginTime = (String) updateAdminDetailed.get("loginTime");
            }
            if (updateAdminDetailed.containsKey("ipaddr")) {
                ipaddr = (String) updateAdminDetailed.get("ipaddr");
            }
            SpringUtils.getBean(SysUserService.class).updateAdminDetailed(adminId, loginTime, ipaddr);
        } catch (Exception e) {
            log.error("更新管理员详细信息失败", e);
        }
    }

    /**
     * 记录登录信息
     *
     * @param jsonString 登录信息
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(RabbitMqConstants.ADMIN_DETAIL_EXCHANGE),
            value = @Queue(RabbitMqConstants.ADMIN_LOGIN_LOG_QUEUE),
            key = RabbitMqConstants.ADMIN_LOGIN_LOG_KEY
    ))
    public void recordLogininfor(String jsonString) {
        LoginLog loginLog = JSON.parseObject(jsonString, LoginLog.class);
        String userAgent = loginLog.getUserAgent();
        String ip = loginLog.getIp();
        String address = AddressUtils.getRealAddressByIP(ip);
        String username = loginLog.getUsername();
        String status = loginLog.getStatus();
        String message = loginLog.getMessage();
        StringBuilder s = new StringBuilder();
        s.append(LogUtils.getBlock(ip));
        s.append(address);
        s.append(LogUtils.getBlock(username));
        s.append(LogUtils.getBlock(status));
        s.append(LogUtils.getBlock(message));
        // 打印信息到日志
        log.info(s.toString());
        // 获取客户端操作系统
        String os = UserAgentUtils.getOperatingSystem(userAgent);
        // 获取客户端浏览器
        String browser = UserAgentUtils.getBrowser(userAgent);
        // 封装对象
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(address);
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            logininfor.setStatus(Constants.SUCCESS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            logininfor.setStatus(Constants.FAIL);
        }
        // 插入数据
        SpringUtils.getBean(AccessRecordsService.class).insertLogininfor(logininfor);
    }
}
