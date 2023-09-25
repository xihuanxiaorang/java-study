package fun.xiaorang.designpattern.adapter;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">圆孔<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/25 9:51
 */
public class RoundHole {
    /**
     * 圆孔半径
     */
    private final double radius;

    public RoundHole(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }


    /**
     * 判断圆钉是否能放入圆孔
     *
     * @param roundPeg 圆钉
     * @return boolean true:能放入 false:不能放入
     */
    public boolean fits(RoundPeg roundPeg) {
        return this.radius >= roundPeg.getRadius();
    }
}
