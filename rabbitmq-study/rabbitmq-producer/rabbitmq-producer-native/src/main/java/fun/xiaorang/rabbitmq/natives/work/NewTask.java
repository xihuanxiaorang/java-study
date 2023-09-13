package fun.xiaorang.rabbitmq.natives.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/4 10:46
 */
public class NewTask {
    private final static String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 主机地址，默认是 "localhost"
        connectionFactory.setHost("42.194.233.222");
        // 通过连接工厂获取一个连接 & 使用连接创建一个通道
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明一个队列，名称为 "hello"
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                // 按回车结束
                String message = scanner.nextLine();
                // 使用默认交换机分发消息给名称为 "hello" 的队列
                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}
