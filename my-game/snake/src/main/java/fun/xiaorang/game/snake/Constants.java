package fun.xiaorang.game.snake;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 21:02
 */
public class Constants {
    /**
     * 游戏窗口宽度
     */
    public static final int GAME_WIDOWS_WIDTH = 806;

    /**
     * 游戏窗口高度
     */
    public static final int GAME_WIDOWS_HEIGHT = 629;

    /**
     * 蛇节点宽度
     */
    public static final int SNAKE_NODE_WIDTH = Img.BODY.getWidth(null);

    /**
     * 蛇节点高度
     */
    public static final int SNAKE_NODE_HEIGHT = Img.BODY.getHeight(null);

    /**
     * 最大的X坐标
     */
    public static final int MAX_X = (GAME_WIDOWS_WIDTH - 6) / SNAKE_NODE_WIDTH;

    /**
     * 最大的Y坐标
     */
    public static final int MAX_Y = (GAME_WIDOWS_HEIGHT - 29) / SNAKE_NODE_HEIGHT;

    /**
     * 默认的游戏速度
     */
    public static final int DEFAULT_GAME_SPEED = 200;

    /**
     * 食物的宽度
     */
    public static final int FOOD_WIDTH = Img.FOOD.getWidth(null);

    /**
     * 食物的高度
     */
    public static final int FOOD_HEIGHT = Img.FOOD.getHeight(null);
}
