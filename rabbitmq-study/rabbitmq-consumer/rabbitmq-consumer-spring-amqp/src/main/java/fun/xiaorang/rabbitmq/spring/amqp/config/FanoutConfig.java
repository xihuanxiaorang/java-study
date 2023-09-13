package fun.xiaorang.rabbitmq.spring.amqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/8 10:49
 */
@Configuration
public class FanoutConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(FanoutConfig.class);

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("itcast.fanout");
    }

    @Bean
    public Queue fanoutQueue1() {
        return new Queue("fanout.queue1");
    }

    @Bean
    public Binding bindingFanoutQueue1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @RabbitListener(queues = {"fanout.queue1"})
    public void listenFanoutQueue1Message(String msg) {
        LOGGER.info("【消费者1】从 {} 队列中收到一条消息：{}", "fanout.queue1", msg);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue("fanout.queue2");
    }

    @Bean
    public Binding bindingFanoutQueue2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

    @RabbitListener(queues = {"fanout.queue2"})
    public void listenFanoutQueue2Message(String msg) {
        LOGGER.info("【消费者2】从 {} 队列中收到一条消息：{}", "fanout.queue2", msg);
    }
}