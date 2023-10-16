package fun.xiaorang.designpattern.factorymethod;

import javax.swing.*;
import java.awt.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/10 7:19
 */
public class WindowsButton implements Button {
    private static final JPanel panel = new JPanel();
    private static final JFrame frame = new JFrame();
    private JButton button;

    @Override
    public void render() {
        // 设置默认的关闭操作，点击关闭按钮时退出程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 创建一个标签
        JLabel label = new JLabel("Hello World!");
        // 设置标签是否不透明
        label.setOpaque(true);
        // 设置标签的背景颜色
        label.setBackground(new Color(235, 233, 126));
        // 设置标签的字体
        label.setFont(new Font("Dialog", Font.BOLD, 44));
        // 设置标签水平居中
        label.setHorizontalAlignment(SwingConstants.CENTER);
        // 设置面板布局
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        // 添加面板到窗口
        frame.getContentPane().add(panel);
        // 添加标签到面板
        panel.add(label);
        this.onClick();
        // 添加按钮到面板
        panel.add(button);
        // 设置窗口大小
        frame.setSize(320, 200);
        // 窗口居中
        frame.setLocationRelativeTo(null);
        // 显示窗口
        frame.setVisible(true);
    }

    @Override
    public void onClick() {
        button = new JButton("Exit");
        // 设置按钮点击事件
        button.addActionListener(e -> {
            // 隐藏窗口
            frame.setVisible(false);
            // 退出程序
            System.exit(0);
        });
    }
}
