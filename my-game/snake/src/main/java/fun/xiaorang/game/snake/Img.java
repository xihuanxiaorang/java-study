package fun.xiaorang.game.snake;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 21:30
 */
public class Img {
    public static Image UP;
    public static Image DOWN;
    public static Image LEFT;
    public static Image RIGHT;
    public static Image BODY;
    public static Image FOOD;

    static {
        ClassLoader classLoader = Img.class.getClassLoader();
        UP = new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/up.png"))).getImage();
        DOWN = new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/down.png"))).getImage();
        LEFT = new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/left.png"))).getImage();
        RIGHT = new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/right.png"))).getImage();
        BODY = new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/body.png"))).getImage();
        FOOD = new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/food.png"))).getImage();
    }
}
