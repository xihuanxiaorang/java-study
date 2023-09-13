package fun.xiaorang.rabbitmq.spring.amqp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/7 0:00
 */
@Configuration
public class SpringAmqpConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringAmqpConfiguration.class);

    @Bean
    public Queue simpleQueue() {
        return new Queue("simple.queue");
    }

    @RabbitListener(queues = {"simple.queue"})
    public void listenSimpleQueueMessage(String msg) {
        LOGGER.info("从 {} 队列中收到一条消息：{}", "simple.queue", msg);
    }

    @Bean
    public Queue workQueue() {
        return new Queue("work.queue");
    }

    @RabbitListener(queues = {"work.queue"})
    public void listenWorkQueueMessage1(String msg) throws InterruptedException {
        LOGGER.info("【消费者1】从 {} 队列中收到一条消息：{}", "work.queue", msg);
        Thread.sleep(20);
    }

    @RabbitListener(queues = {"work.queue"})
    public void listenWorkQueueMessage2(String msg) throws InterruptedException {
        LOGGER.error("【消费者2】从 {} 队列中收到一条消息：{}", "work.queue", msg);
        Thread.sleep(200);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue("direct.queue1"),
                    exchange = @Exchange(name = "itcast.direct"), key = {"red", "blue"})
    })
    public void listenDirectQueueMessage1(String msg) {
        LOGGER.info("【消费者1】从 {} 队列中收到一条消息：{}", "direct.queue1", msg);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue("direct.queue2"),
                    exchange = @Exchange(name = "itcast.direct"), key = {"red", "yellow"})
    })
    public void listenDirectQueueMessage2(String msg) {
        LOGGER.info("【消费者2】从 {} 队列中收到一条消息：{}", "direct.queue2", msg);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue("topic.queue1"),
                    exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC), key = {"china.#"})
    })
    public void listenTopicQueueMessage1(String msg) {
        LOGGER.info("【消费者1】从 {} 队列中收到一条消息：{}", "topic.queue1", msg);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue("topic.queue2"),
                    exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC), key = {"#.news"})
    })
    public void listenTopicQueueMessage2(String msg) {
        LOGGER.info("【消费者2】从 {} 队列中收到一条消息：{}", "topic.queue2", msg);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queuesToDeclare = {@org.springframework.amqp.rabbit.annotation.Queue("object.queue")})
    public void listenObjectQueueMessage(Map<String, Object> msg) {
        LOGGER.info("【消费者】从 {} 队列中收到一条消息：{}", "object.queue", msg);
    }
}
