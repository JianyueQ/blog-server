package com.blog.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * @author 31373
 */
@Configuration
@EnableRabbit
public class RabbitMqConfig {


    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 配置 RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 显式设置消息转换器
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 开启消息确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                // 消息发送成功
                log.info("消息发送成功");
            } else {
                // 消息发送失败
                log.error("消息发送失败: {}", cause);
            }
        });
        // 开启失败回调
        rabbitTemplate.setReturnsCallback(returned -> {
            // 消息路由失败处理
        });
        log.info("配置RabbitTemplate完成");
        return rabbitTemplate;
    }


}
