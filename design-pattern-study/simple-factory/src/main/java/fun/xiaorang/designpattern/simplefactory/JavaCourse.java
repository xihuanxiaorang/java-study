package fun.xiaorang.designpattern.simplefactory;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/10 15:22
 */
public class JavaCourse implements ICourse {
    @Override
    public void record() {
        System.out.println("录制 Java 课程");
    }
}
