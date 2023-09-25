package fun.xiaorang.designpattern.adapter;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">圆钉<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/25 9:52
 */
public class RoundPeg {
    /**
     * 圆钉半径
     */
    private double radius;

    public RoundPeg() {
    }

    public RoundPeg(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}
