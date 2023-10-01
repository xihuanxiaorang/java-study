package fun.xiaorang.game._2048;

import java.awt.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/1 13:54
 */
public class Constants {
    /**
     * 卡片行数
     */
    public static final int ROWS = 4;
    /**
     * 卡片列数
     */
    public static final int COLS = 4;
    /**
     * 默认的卡片宽度
     */
    public static final int DEFAULT_CARD_WIDTH = 80;
    /**
     * 默认的卡片高度
     */
    public static final int DEFAULT_CARD_HEIGHT = 80;
    /**
     * 卡片与面板的间距
     */
    public static final int CARD_PANEL_PADDING = 15;
    /**
     * 卡片间距
     */
    public static final int CARD_PADDING = 10;
    /**
     * 游戏面板宽度
     */
    public static final int GAME_PANEL_WIDTH = DEFAULT_CARD_WIDTH * COLS + CARD_PADDING * (COLS - 1) + (CARD_PANEL_PADDING << 1);
    /**
     * 游戏窗口宽度
     */
    public static final int GAME_WIDOWS_WIDTH = GAME_PANEL_WIDTH;
    /**
     * 游戏面板高度
     */
    public static final int GAME_PANEL_HEIGHT = DEFAULT_CARD_HEIGHT * ROWS + CARD_PADDING * (ROWS - 1) + (CARD_PANEL_PADDING << 1);
    /**
     * 游戏窗口高度
     */
    public static final int GAME_WIDOWS_HEIGHT = GAME_PANEL_HEIGHT;
    /**
     * 默认的游戏速度
     */
    public static final int DEFAULT_GAME_SPEED = 200;
    /**
     * 游戏面板默认背景颜色
     */
    public static final Color DEFAULT_GAME_PANEL_BACKGROUND_COLOR = new Color(0xBBADA0);
}
