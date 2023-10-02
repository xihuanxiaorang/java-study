package fun.xiaorang.game.popstar.core;

import fun.xiaorang.game.popstar.manager.ImageManager;

import java.awt.*;

import static fun.xiaorang.game.popstar.core.Constants.STAR_HEIGHT;
import static fun.xiaorang.game.popstar.core.Constants.STAR_WIDTH;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 21:47
 */
public class Star {
    private final int i;
    private final int j;
    private final int x;
    private final int y;
    private final Color color;
    private boolean selected = false;

    public Star(int i, int j) {
        this(i, j, Color.random());
    }

    public Star(int i, int j, Color color) {
        this.i = i;
        this.j = j;
        this.color = color;
        this.x = j * STAR_WIDTH;
        this.y = i * STAR_HEIGHT;
    }

    public void draw(Graphics g) {
        g.drawImage(color.getImage(), x, y, null);
        if (selected) {
            g.drawImage(ImageManager.STAR_SELECTED, x, y, null);
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
