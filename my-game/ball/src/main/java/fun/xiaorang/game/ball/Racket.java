package fun.xiaorang.game.ball;

import java.awt.*;

import static fun.xiaorang.game.ball.Constants.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/29 9:47
 */
public class Racket {
    private final Point point;
    private final int width = DEFAULT_RACKET_WIDTH;
    private final int height = DEFAULT_RACKET_HEIGHT;
    private int speed = 0;

    public Racket() {
        this.point = new Point(GAME_PANEL_WIDTH - width >> 1, GAME_PANEL_HEIGHT - height - DEFAULT_RACKET_BOTTOM_PADDING);
    }

    public void draw(Graphics g) {
        this.move();
        g.setColor(Color.RED);
        g.fillRect(point.x, point.y, width, height);
    }

    private void move() {
        this.point.x += speed;
        // 检查挡板是否碰到左边界
        if (point.x < 0) {
            point.x = 0;
        }
        int max = GAME_PANEL_WIDTH - width;
        // 检查挡板是否碰到右边界
        if (point.x > max) {
            point.x = max;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Point getPoint() {
        return point;
    }

    public int getWidth() {
        return width;
    }
}
