package fun.xiaorang.designpattern.simplefactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/10 15:24
 */
public class CourseFactory {
    private CourseFactory() {
    }

    public static ICourse createCourse() {
        try (InputStream in = CourseFactory.class.getResourceAsStream("/simple-factory.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            String type = properties.getProperty("type");
            return (ICourse) Class.forName(type).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
