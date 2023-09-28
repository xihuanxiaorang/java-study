package fun.xiaorang.game.snake;

import java.awt.*;
import java.util.Random;

import static fun.xiaorang.game.snake.Constants.FOOD_HEIGHT;
import static fun.xiaorang.game.snake.Constants.FOOD_WIDTH;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/28 2:13
 */
public class Food {
    private Point point = new Point();

    public Food(Snake snake) {
        this.random(snake);
    }

    public void draw(Graphics g) {
        g.drawImage(Img.FOOD, point.x * FOOD_WIDTH, point.y * FOOD_HEIGHT, null);
    }

    public void random(Snake snake) {
        Random random = new Random();
        // 循环生成食物的坐标，直到找到合适的位置
        do {
            this.point.x = random.nextInt(Constants.MAX_X);
            this.point.y = random.nextInt(Constants.MAX_Y);
        } while (snake.isSnakeBody(this.point)); // 检查食物是否与蛇的身体重叠
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
