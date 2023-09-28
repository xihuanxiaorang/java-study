package fun.xiaorang.game.snake;

import java.awt.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 21:02
 */
public class Constants {
    /**
     * 游戏标题宽度
     */
    public static final int TITLE_WIDTH = Img.TITLE.getWidth(null);
    /**
     * 游戏标题高度
     */
    public static final int TITLE_HEIGHT = Img.TITLE.getHeight(null);
    /**
     * 蛇节点宽度
     */
    public static final int SNAKE_NODE_WIDTH = Img.BODY.getWidth(null);
    /**
     * 蛇节点高度
     */
    public static final int SNAKE_NODE_HEIGHT = Img.BODY.getHeight(null);
    /**
     * 游戏面板宽度
     */
    public static final int GAME_PANEL_WIDTH = TITLE_WIDTH;
    /**
     * 最大的X坐标
     */
    public static final int MAX_X = GAME_PANEL_WIDTH / SNAKE_NODE_WIDTH;
    /**
     * 游戏面板高度
     */
    public static final int GAME_PANEL_HEIGHT = 600;
    /**
     * 边距
     */
    public static final int PADDING = 15;
    /**
     * 游戏窗口宽度
     */
    public static final int GAME_WIDOWS_WIDTH = TITLE_WIDTH + (PADDING << 1);
    /**
     * 游戏窗口高度
     */
    public static final int GAME_WIDOWS_HEIGHT = PADDING * 3 + TITLE_HEIGHT + GAME_PANEL_HEIGHT;
    /**
     * 最大的Y坐标
     */
    public static final int MAX_Y = GAME_PANEL_HEIGHT / SNAKE_NODE_HEIGHT;
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
    /**
     * 默认字体
     */
    public static final Font DEFAULT_FONT = new Font("宋体", Font.BOLD, 25);
}
