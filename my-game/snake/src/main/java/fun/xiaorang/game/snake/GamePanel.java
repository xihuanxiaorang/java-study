package fun.xiaorang.game.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static fun.xiaorang.game.snake.Constants.*;
import static fun.xiaorang.game.snake.State.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 21:07
 */
public class GamePanel extends JPanel implements ActionListener {
    /**
     * 定时器，用于定时重绘，实现动画效果
     */
    private final Timer timer = new Timer(DEFAULT_GAME_SPEED, this);
    private Snake snake;
    private Food food;
    private State state = PAUSE;

    public GamePanel() {
        // 初始化
        init();
        // 开始游戏
        start();
    }

    private void start() {
        // 初始化蛇
        this.snake = new Snake();
        // 初始化食物
        this.food = new Food(snake);
    }

    private void init() {
        // 自由布局
        this.setLayout(null);
        // 设置面板大小以及位置
        this.setBounds(PADDING, (PADDING << 1) + TITLE_HEIGHT, GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT);
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
        // 绘制蛇
        snake.draw(g, this.food, this);
        // 绘制食物
        food.draw(g);
        // 绘制游戏状态
        drawState(g);
    }

    private void drawState(Graphics g) {
        if (this.state == PAUSE) {
            drawText(g, "按下空格键开始游戏");
        } else if (this.state == OVER) {
            drawText(g, "游戏结束，按下空格键重新开始");
        }
    }

    private void drawText(Graphics g, String text) {
        g.setColor(Color.WHITE);
        g.setFont(DEFAULT_FONT);
        // 居中显示
        int x = (this.getWidth() - g.getFontMetrics().stringWidth(text)) >> 1;
        int y = (this.getHeight() - g.getFontMetrics().getHeight()) >> 1;
        // 绘制文字
        g.drawString(text, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 重绘
        repaint();
    }

    public void changeState() {
        if (this.state == RUNNING) {
            this.state = PAUSE;
            // 停止播放背景音乐
            SoundManager.stopBackgroundMusic();
        } else if (this.state == PAUSE) {
            this.state = RUNNING;
            // 播放背景音乐
            SoundManager.playBackgroundMusic();
        } else if (this.state == OVER) {
            // 设置游戏状态为运行中
            this.state = RUNNING;
            // 重新开始游戏
            this.restart();
            // 停止播放游戏结束音乐
            SoundManager.stopGameOverMusic();
            // 播放背景音乐
            SoundManager.playBackgroundMusic();
        }
    }

    private void restart() {
        // 开始游戏
        start();
    }

    public void changeDirection(Direction direction) {
        this.snake.changeDirection(direction);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
