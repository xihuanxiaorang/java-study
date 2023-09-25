package fun.xiaorang.designpattern.adapter;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">方钉到圆孔的适配器<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/25 9:58
 */
public class SquarePegAdapter extends RoundPeg {
    private final SquarePeg squarePeg;

    public SquarePegAdapter(SquarePeg squarePeg) {
        this.squarePeg = squarePeg;
    }

    @Override
    public double getRadius() {
        // 求方钉的对角线长度 = 边长 * √2
        double diagonal = squarePeg.getWidth() * Math.sqrt(2);
        // 圆钉的半径等于方钉的对角线长度的一半
        return diagonal / 2;
    }
}
