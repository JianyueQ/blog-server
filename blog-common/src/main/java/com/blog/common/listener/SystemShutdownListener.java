package com.blog.common.listener;

import com.blog.common.utils.SystemUptimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 *系统关闭监听器
 * 用于在应用关闭时保存系统累计运行时长
 * 
 * @author 31373
 */
@Component
public class SystemShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(SystemShutdownListener.class);
    @Autowired
    private SystemUptimeUtils systemUptimeUtils;
    
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("检测到系统关闭事件，正在保存累计运行时长数据...");
        // 保存当前会话的累计运行时长
        systemUptimeUtils.updateAndSaveTotalUptime();
        log.info("系统累计运行时长数据保存完成");
    }
}