package fun.xiaorang.rabbitmq.spring.amqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/11 17:59
 */
@Configuration
public class DelayMessageConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelayMessageConfig.class);

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = "delay.queue"),
                    exchange = @Exchange(name = "delay.direct", delayed = "true"),
                    key = {"delay"}
            )
    })
    public void listenDelayMessage(String message) {
        LOGGER.info("从 delay.queue 队列中收到一条延迟消息：{}", message);
    }
}
