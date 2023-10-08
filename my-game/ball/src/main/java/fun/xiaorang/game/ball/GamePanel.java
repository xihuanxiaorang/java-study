package fun.xiaorang.game.ball;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static fun.xiaorang.game.ball.Constants.*;
import static fun.xiaorang.game.ball.State.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/29 8:55
 */
public class GamePanel extends JPanel implements ActionListener {
    private Ball ball = new Ball();
    private Racket racket = new Racket();
    private State state = PAUSE;

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
        // 添加键盘监听
        this.addKeyListener(new PlayerControl(this));
        // 将当前面板标记为可获得焦点的组件
        this.setFocusable(true);
        // 请求当前面板在窗口中获得焦点
        this.requestFocusInWindow();
        // 通过请求焦点来确保当前面板获得焦点（这行通常不会影响焦点的设置，但可以作为一种额外的措施）
        this.requestFocus();
        // 创建定时器，用于定时重绘，实现动画效果
        Timer timer = new Timer(DEFAULT_GAME_SPEED, this);
        // 启动定时器
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制小球
        ball.draw(g, this);
        // 绘制球拍
        racket.draw(g);
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
        g.setColor(DEFAULT_FONT_COLOR);
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

    public Racket getRacket() {
        return racket;
    }

    public void changeState() {
        if (this.state == RUNNING) {
            this.state = PAUSE;
        } else if (this.state == PAUSE) {
            this.state = RUNNING;
        } else if (this.state == OVER) {
            // 设置游戏状态为运行中
            this.state = RUNNING;
            // 重新开始游戏
            this.restart();
        }
    }

    private void restart() {
        // 开始游戏
        start();
    }

    private void start() {
        // 初始化小球
        this.ball = new Ball();
        // 初始化挡板
        this.racket = new Racket();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
