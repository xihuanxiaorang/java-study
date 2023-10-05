package fun.xiaorang.game.ball;

import java.awt.*;

import static fun.xiaorang.game.ball.Constants.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/29 9:05
 */
public class Ball {
    /**
     * 小球半径
     */
    private final int radius = DEFAULT_BALL_RADIUS;
    /**
     * 小球中心点坐标
     */
    private final Point point = new Point(GAME_PANEL_WIDTH >> 1, GAME_PANEL_HEIGHT >> 1);
    /**
     * 小球Y轴速度
     */
    private int speedY = DEFAULT_BALL_SPEED_Y;
    /**
     * 小球X轴速度
     */
    private int speedX = DEFAULT_BALL_SPEED_X;

    public void draw(Graphics g, GamePanel gamePanel) {
        this.move(gamePanel);
        g.setColor(Color.ORANGE);
        g.fillOval(point.x - radius, point.y - radius, radius << 1, radius << 1);
    }

    private void move(GamePanel gamePanel) {
        // 检查小球是否碰到上下边界，如果碰到上下边界，那么Y轴速度取反
        if (point.y - radius <= 0 || point.y + radius >= GAME_PANEL_HEIGHT) {
            speedY = -speedY;
        }
        // 检查小球是否碰到左右边界，如果碰到左右边界，那么X轴速度取反
        if (point.x - radius <= 0 || point.x + radius >= GAME_PANEL_WIDTH) {
            speedX = -speedX;
        }
        // 更新小球的坐标
        point.x += speedX;
        point.y += speedY;
    }
}
