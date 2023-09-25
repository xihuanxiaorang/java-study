package fun.xiaorang.designpattern.adapter;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">方钉<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/25 9:55
 */
public class SquarePeg {
    /**
     * 方钉宽度
     */
    private final double width;

    public SquarePeg(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    /**
     * 获取方钉的面积
     *
     * @return double 面积
     */
    public double getSquare() {
        return Math.pow(this.width, 2);
    }
}
