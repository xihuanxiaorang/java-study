package fun.xiaorang.rabbitmq.natives.exchange.direct;

import com.rabbitmq.client.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/3 22:17
 */
public class ReceiveLogsDirect2 {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("42.194.233.222");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明一个名称为 "direct_logs" 的扇形交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明一个临时队列，队列的名称是随机的，当没有消费者监听该队列时，队列会自动删除
        String queueName = channel.queueDeclare().getQueue();
        // 将队列与交换机进行绑定，此时绑定用的路由键为 "error"
        channel.queueBind(queueName, EXCHANGE_NAME, "error");

        System.out.println(" [*] Waiting for messages");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("F:\\logs_from_rabbit.log"))) {
                bufferedWriter.write(message);
            }
            System.out.println(" [x] write to the file succeeded");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
