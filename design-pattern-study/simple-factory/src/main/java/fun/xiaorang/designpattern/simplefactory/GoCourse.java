package fun.xiaorang.designpattern.simplefactory;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/10 16:06
 */
public class GoCourse implements ICourse {
    @Override
    public void record() {
        System.out.println("录制 Go 课程");
    }
}
