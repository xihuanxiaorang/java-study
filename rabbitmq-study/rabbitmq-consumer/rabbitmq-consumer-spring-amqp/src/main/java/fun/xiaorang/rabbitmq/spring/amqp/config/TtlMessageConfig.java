package fun.xiaorang.rabbitmq.spring.amqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/11 14:23
 */
@Configuration
public class TtlMessageConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(TtlMessageConfig.class);

    @Bean
    public org.springframework.amqp.core.Queue ttlQueue() {
        return QueueBuilder
                // 指定队列名称以及持久化
                .durable("ttl.queue")
                // 设置队列的超时时间为 10 秒，即配置 x-message-ttl 属性
                .ttl(10000)
                // 指定死信交换机
                .deadLetterExchange("dl.direct")
                // 指定死信路由键
                .deadLetterRoutingKey("dl")
                .build();
    }

    @Bean
    public DirectExchange ttlExchange() {
        return new DirectExchange("ttl.direct");
    }

    @Bean
    public Binding ttlBinding() {
        return BindingBuilder.bind(ttlQueue()).to(ttlExchange()).with("ttl");
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = "dl.queue"),
                    exchange = @Exchange(name = "dl.direct"),
                    key = {"dl"}
            )
    })
    public void listenDeadMessage(String message) {
        LOGGER.info("从 dl.queue 队列中收到一条延迟消息：{}", message);
    }
}
