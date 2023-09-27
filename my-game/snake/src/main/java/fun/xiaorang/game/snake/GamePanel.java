package fun.xiaorang.game.snake;

import javax.swing.*;
import java.awt.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 21:07
 */
public class GamePanel extends JPanel {
    private final Snake snake;

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        snake.draw(g);
    }
}
