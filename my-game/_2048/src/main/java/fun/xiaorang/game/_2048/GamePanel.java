package fun.xiaorang.game._2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static fun.xiaorang.game._2048.Constants.*;
import static fun.xiaorang.game._2048.GameState.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/1 13:54
 */
public class GamePanel extends JPanel implements ActionListener {
    private final GameData gameData = new GameData();

    public GamePanel() {
        // 初始化
        this.init();
    }

    private void init() {
        // 自由布局
        this.setLayout(null);
        // 设置面板大小以及位置
        this.setBounds(0, 0, GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT);
        // 设置游戏面板的背景颜色
        this.setBackground(DEFAULT_GAME_PANEL_BACKGROUND_COLOR);
        // 将当前面板标记为可获得焦点的组件
        this.setFocusable(true);
        // 请求当前面板在窗口中获得焦点
        this.requestFocusInWindow();
        // 通过请求焦点来确保当前面板获得焦点（这行通常不会影响焦点的设置，但可以作为一种额外的措施）
        this.requestFocus();
        // 为当前游戏面板添加键盘监听器
        this.addKeyListener(new PlayerControl(gameData));
        // 创建定时器，用于定时重绘，实现动画效果
        Timer timer = new Timer(DEFAULT_GAME_SPEED, this);
        // 启动定时器
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制卡片
        this.gameData.getBox().draw(g, this.gameData);
        // 绘制游戏状态
        this.drawGameState(g);
    }

    private void drawGameState(Graphics g) {
        GameState gameState = this.gameData.getGameState();
        if (gameState == PAUSE) {
            drawText(g, "按下空格键开始游戏");
        } else if (gameState == OVER) {
            drawText(g, "游戏结束，按下空格键重新开始");
        } else if (gameState == WIN) {
            drawText(g, "游戏胜利！按下空格键重新开始");
        }
    }

    private void drawText(Graphics g, String text) {
        g.setColor(Color.WHITE);
        g.setFont(DEFAULT_STATE_FONT);
        // 居中显示
        int x = (this.getWidth() - g.getFontMetrics().stringWidth(text)) >> 1;
        int y = (this.getHeight() + g.getFontMetrics().getHeight()) >> 1;
        // 绘制文字
        g.drawString(text, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 重绘
        repaint();
    }
}
