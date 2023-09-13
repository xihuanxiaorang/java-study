package fun.xiaorang.rabbitmq.spring.amqp;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() {
        String queueName = "simple.queue";
        String message = "hello, spring amqp!";
        rabbitTemplate.convertAndSend(queueName, message);
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
}
