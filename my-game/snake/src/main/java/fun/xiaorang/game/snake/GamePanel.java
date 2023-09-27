package fun.xiaorang.game.snake;

import javax.swing.*;
import java.awt.*;

import static fun.xiaorang.game.snake.Constants.DEFAULT_GAME_SPEED;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 21:07
 */
public class GamePanel extends JPanel {
    private final Snake snake;
    /**
     * 定时器，用于定时重绘，实现动画效果
     */
    private final Timer timer = new Timer(DEFAULT_GAME_SPEED, e -> {
        // 重绘
        repaint();
    });

    public GamePanel() {
        // 初始化
        init();
        // 初始化蛇
        snake = new Snake();
    }

    private void init() {
        // 自由布局
        this.setLayout(null);
        // 设置背景颜色为黑色
        this.setBackground(Color.BLACK);
        // 添加键盘监听
        this.addKeyListener(new PlayerControl(this));
        // 将当前面板标记为可获得焦点的组件
        this.setFocusable(true);
        // 请求当前面板在窗口中获得焦点
        this.requestFocusInWindow();
        // 通过请求焦点来确保当前面板获得焦点（这行通常不会影响焦点的设置，但可以作为一种额外的措施）
        this.requestFocus();
        // 启动定时器
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        snake.draw(g);
    }

    public Snake getSnake() {
        return snake;
    }
}
