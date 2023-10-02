package fun.xiaorang.game.popstar.core;

import fun.xiaorang.game.popstar.manager.ImageManager;

import java.awt.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 21:49
 */
public enum Color {
    BLUE {
        @Override
        public Image getImage() {
            return ImageManager.STAR_BLUE;
        }
    }, GREEN {
        @Override
        public Image getImage() {
            return ImageManager.STAR_GREEN;
        }
    }, PURPLE {
        @Override
        public Image getImage() {
            return ImageManager.STAR_PURPLE;
        }
    }, RED {
        @Override
        public Image getImage() {
            return ImageManager.STAR_RED;
        }
    }, YELLOW {
        @Override
        public Image getImage() {
            return ImageManager.STAR_YELLOW;
        }
    };

    /**
     * 随机获取颜色，用于初始化星星
     *
     * @return 颜色
     */
    public static Color random() {
        return values()[(int) (Math.random() * values().length)];
    }

    /**
     * 获取图片，用于绘制星星，每个颜色对应一张图片
     *
     * @return 图片
     */
    public abstract Image getImage();
}
