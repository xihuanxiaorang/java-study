package fun.xiaorang.rabbitmq.spring.amqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/11 21:39
 */
@Configuration
public class LazyQueueConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(LazyQueueConfig.class);

    @RabbitListener(queuesToDeclare = {
            @Queue(
                    name = "lazy.queue",
                    durable = "true",
                    arguments = {@Argument(name = "x-queue-mode", value = "lazy")}
            )
    })
    public void listenLazyMessage(String message) {
        LOGGER.info("从 lazy.queue 队列中收到一条消息：{}", message);
    }
}
