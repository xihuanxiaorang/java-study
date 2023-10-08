package fun.xiaorang.game.popstar.core;

import fun.xiaorang.game.popstar.manager.ImageManager;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 21:05
 */
public class Constants {
    /**
     * 行数
     */
    public static final int ROWS = 10;
    /**
     * 列数
     */
    public static final int COLS = 10;
    /**
     * 星星的宽度
     */
    public static final int STAR_WIDTH = ImageManager.STAR_RED.getWidth(null);
    /**
     * 星星的高度
     */
    public static final int STAR_HEIGHT = ImageManager.STAR_RED.getHeight(null);
    /**
     * 游戏面板的宽度
     */
    public static final int GAME_PANEL_WIDTH = COLS * STAR_WIDTH;
    /**
     * 游戏场景的宽度
     */
    public static final int GAME_SCENE_WIDTH = GAME_PANEL_WIDTH;
    /**
     * 游戏面板的高度
     */
    public static final int GAME_PANEL_HEIGHT = ROWS * STAR_HEIGHT;
    /**
     * 游戏场景的高度
     */
    public static final int GAME_SCENE_HEIGHT = GAME_PANEL_HEIGHT;
    /**
     * 默认帧率，每秒60帧
     */
    public static final int DEFAULT_FRAME_RATE = 60;
}
