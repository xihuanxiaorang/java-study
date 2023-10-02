package fun.xiaorang.game.popstar.scene;

import fun.xiaorang.game.popstar.core.Star;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static fun.xiaorang.game.popstar.core.Constants.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 21:14
 */
public class GamePanel extends JPanel implements ActionListener {
    private static final Star[][] STARS = new Star[ROWS][COLS];

    public GamePanel() {
        // 初始化
        this.init();
        // 初始化星星
        this.initStars();
    }

    private void initStars() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                STARS[i][j] = new Star(i, j);
            }
        }
    }

    private void init() {
        // 取消默认布局，使用自由布局
        this.setLayout(null);
        // 设置面板大小以及位置
        this.setBounds(0, 0, GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT);
        // 设置面板透明
        this.setOpaque(false);
        // 创建定时器，用于刷新面板，实现动画效果，每秒刷新60次
        Timer timer = new Timer(1000 / DEFAULT_FRAME_RATE, this);
        // 启动定时器
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制星星
        this.drawStars(g);
    }

    private void drawStars(Graphics g) {
        // 遍历所有星星
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // 绘制星星
                STARS[i][j].draw(g);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 重绘面板
        this.repaint();
    }
}
