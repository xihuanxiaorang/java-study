package fun.xiaorang.springboot.annotation.pojo;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/17 0:07
 */
public class Teacher {
    /**
     * String类型
     */
    @Value("小让")
    private String name;

    @Value("2023-05-17 00:10:05")
    private Date birthday;

    /**
     * SpEL表达式
     */
    @Value("#{30-3}")
    private int age;

    @Value("#{T(Math).random()}")
    private double salary;

    /**
     * 从环境变量(Environment)中取值
     */
    @Value("${teacher.workDate}")
    private Date workDate;

    /**
     * 从环境变量中取值，如果没有该配置，则给一个默认值
     */
    @Value("${teacher.teach:english}")
    private String teach;

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", salary=" + salary +
                ", workDate='" + workDate + '\'' +
                ", teach='" + teach + '\'' +
                '}';
    }
}
