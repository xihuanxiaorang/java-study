package fun.xiaorang.rabbitmq.spring.amqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/9 17:05
 */
@Configuration
public class CommonConfig implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfig.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            Message message = returnedMessage.getMessage();
            String exchange = returnedMessage.getExchange();
            int replyCode = returnedMessage.getReplyCode();
            String replyText = returnedMessage.getReplyText();
            String routingKey = returnedMessage.getRoutingKey();
            // 判断是否为延迟消息
            Integer delay = Optional.ofNullable(message.getMessageProperties().getReceivedDelay()).orElse(0);
            if (delay > 0) {
                return;
            }
            // 投递失败，记录日志
            LOGGER.error("消息路由失败，应答码：{}，原因：{}， 交换机：{}，路由键：{}，消息：{}", replyCode, replyText, exchange, routingKey, message);
            // 如果数据可靠性要求高，可以进行失败重试，使用 spring-retry 重发消息；
        });
    }
}
