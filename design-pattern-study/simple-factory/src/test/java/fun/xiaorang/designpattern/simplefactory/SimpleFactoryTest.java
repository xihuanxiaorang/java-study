package fun.xiaorang.designpattern.simplefactory;

import org.junit.jupiter.api.Test;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/10 15:26
 */
class SimpleFactoryTest {
    @Test
    public void test() {
        ICourse course = CourseFactory.createCourse();
        course.record();
    }
}