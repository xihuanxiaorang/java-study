package fun.xiaorang.game.ball;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static fun.xiaorang.game.ball.Constants.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/29 8:55
 */
public class GamePanel extends JPanel implements ActionListener {
    public GamePanel() {
        // 开始游戏
        this.start();
        // 初始化
        this.init();
    }

    private void init() {
        // 设置当前面板为自由布局
        this.setLayout(null);
        // 设置面板大小以及位置
        this.setBounds(0, 0, GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT);
        // 设置背景颜色为黑色
        this.setBackground(Color.BLACK);
        // 创建定时器，用于定时重绘，实现动画效果
        Timer timer = new Timer(DEFAULT_GAME_SPEED, this);
        // 启动定时器
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 重绘
        repaint();
    }

    private void start() {
    }
}
