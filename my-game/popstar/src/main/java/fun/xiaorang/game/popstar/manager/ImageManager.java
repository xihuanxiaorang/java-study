package fun.xiaorang.game.popstar.manager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 20:41
 */
public class ImageManager {
    public static final Image BACKGROUND;
    public static final Image STAR_BLUE;
    public static final Image STAR_GREEN;
    public static final Image STAR_PURPLE;
    public static final Image STAR_RED;
    public static final Image STAR_YELLOW;
    public static final Image STAR_SELECTED;

    static {
        // 加载背景图片
        BACKGROUND = load("background.png");
        // 加载蓝色星星图片
        STAR_BLUE = load("star_blue.png");
        // 加载绿色星星图片
        STAR_GREEN = load("star_green.png");
        // 加载紫色星星图片
        STAR_PURPLE = load("star_purple.png");
        // 加载红色星星图片
        STAR_RED = load("star_red.png");
        // 加载黄色星星图片
        STAR_YELLOW = load("star_yellow.png");
        // 加载选中星星图片
        STAR_SELECTED = load("star_select.png");
    }

    /**
     * 加载图片
     *
     * @param fileName 文件名
     * @return 图片
     */
    private static Image load(String fileName) {
        Image image;
        try (InputStream is = ImageManager.class.getResourceAsStream("/images/" + fileName)) {
            image = ImageIO.read(Objects.requireNonNull(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(image).orElseThrow(() -> new RuntimeException("加载图片失败"));
    }
}
