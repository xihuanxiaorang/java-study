package fun.xiaorang.rabbitmq.spring.amqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/11 1:45
 */
@Configuration
public class RepublishMessageConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepublishMessageConfig.class);

    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = "error.queue"),
                    exchange = @Exchange(name = "error.direct"),
                    key = {"error"}
            )
    })
    public void listenErrorMessage(String message) {
        LOGGER.error("从 error.queue 队列中收到一条异常信息：{}，请及时处理！", message);
    }
}
