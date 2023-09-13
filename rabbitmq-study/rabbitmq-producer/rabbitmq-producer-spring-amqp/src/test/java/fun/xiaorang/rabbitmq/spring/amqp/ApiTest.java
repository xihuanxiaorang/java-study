package fun.xiaorang.rabbitmq.spring.amqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/6 23:27
 */
@SpringBootTest
public class ApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTest.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() {
        // 消息体
        String message = "hello, spring amqp!";
        // 全局唯一的消息 ID，需要封装到 CorrelationData 中
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 添加 callback
        correlationData.getFuture().addCallback(result -> {
            assert result != null;
            if (result.isAck()) {
                // ack，消息成功投递到交换机
                LOGGER.info("消息成功投递到交换机，ID：{}", correlationData.getId());
            } else {
                // nack，消息投递到交换机失败
                LOGGER.error("消息投递到交换机失败，ID：{}，原因：{}", correlationData.getId(), result.getReason());
                // 重发消息
            }
        }, ex -> {
            // 记录日志
            LOGGER.error("消息发送异常，ID：{}，原因：{}", correlationData.getId(), ex.getMessage());
            // 重发消息
        });
        rabbitTemplate.convertAndSend("", "simple.queue1", message, correlationData);
    }

    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        String message = "hello, message__";
        for (int i = 1; i <= 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    @Test
    public void testSendMessage2FanoutExchange() {
        String exchangeName = "itcast.fanout";
        String message = "hello, every one!";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    @Test
    public void testSendMessage2DirectExchange() {
        String exchangeName = "itcast.direct";
        String message = "hello, ";
        Stream.of("red", "blue", "yellow").forEach(color ->
                rabbitTemplate.convertAndSend(exchangeName, color, message + color));
    }

    @Test
    public void testSendMessage2TopicExchange() {
        String exchangeName = "itcast.topic";
        Map<String, String> map = new HashMap<>(2);
        map.put("china.news", "中华人民共和国成立啦！");
        map.put("china.weather", "今天阳光明媚");
        map.forEach((routingKey, message) -> rabbitTemplate.convertAndSend(exchangeName, routingKey, message));
    }

    @Test
    public void testSendMessage2ObjectQueue() {
        String queueName = "object.queue";
        Map<String, Object> message = new HashMap<>(2);
        message.put("name", "xiaorang");
        message.put("age", 18);
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testSendDurableMessage() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString("hello, spring amqp");
        Message message = MessageBuilder
                .withBody(content.getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        rabbitTemplate.convertAndSend("simple.queue", message);
    }

    @Test
    public void testTTLQueue() {
        // 发送消息
        rabbitTemplate.convertAndSend("ttl.direct", "ttl", "hello, ttl queue");
        // 记录日志
        LOGGER.info("发送消息成功");
    }

    @Test
    public void testTTLMessage() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString("hello, ttl message");
        Message message = MessageBuilder
                .withBody(content.getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                // 设置过期时间
                .setExpiration("5000")
                .build();
        // 发送消息
        rabbitTemplate.convertAndSend("ttl.direct", "ttl", message);
        // 记录日志
        LOGGER.info("发送消息成功");
    }

    @Test
    public void testDelayMessage() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString("hello, delay message");
        Message message = MessageBuilder
                .withBody(content.getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                // 设置过期时间
                .setHeader("x-delay", 5000)
                .build();
        // 发送消息
        rabbitTemplate.convertAndSend("delay.direct", "delay", message);
        // 记录日志
        LOGGER.info("发送消息成功");
    }
}
