package fun.xiaorang.adapter;

import fun.xiaorang.designpattern.adapter.RoundHole;
import fun.xiaorang.designpattern.adapter.RoundPeg;
import fun.xiaorang.designpattern.adapter.SquarePeg;
import fun.xiaorang.designpattern.adapter.SquarePegAdapter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/25 10:10
 */
public class ApiTest {
    private static final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_00() {
        // 创建圆孔
        RoundHole roundHole = new RoundHole(5);
        // 创建圆钉
        RoundPeg roundPeg = new RoundPeg(5);
        // 判断圆钉是否能放入圆孔
        logger.info("半径为 {} 的圆钉是否能放入半径为 {} 的圆孔中:{}", roundPeg.getRadius(), roundHole.getRadius(), roundHole.fits(roundPeg));

        // 创建方钉
        SquarePeg smallSqPeg = new SquarePeg(2);
        SquarePeg largeSqPeg = new SquarePeg(20);
        // 创建方钉到圆孔的适配器
        SquarePegAdapter smallSqPegAdapter = new SquarePegAdapter(smallSqPeg);
        SquarePegAdapter largeSqPegAdapter = new SquarePegAdapter(largeSqPeg);
        // 判断方钉是否能放入圆孔
        logger.info("宽度为 {} 的方钉是否能放入半径为 {} 的圆孔中:{}", smallSqPeg.getWidth(), roundHole.getRadius(), roundHole.fits(smallSqPegAdapter));
        logger.info("宽度为 {} 的方钉是否能放入半径为 {} 的圆孔中:{}", largeSqPeg.getWidth(), roundHole.getRadius(), roundHole.fits(largeSqPegAdapter));
    }
}
