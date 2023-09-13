package fun.xiaorang.spring.pojo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/24 12:05
 */
public class Student implements InitializingBean, DisposableBean {
    private String name;
    private String age;
    private Integer sex;

    private Student() {
        System.out.println("Student -> 无参构造方法");
    }

    public Student(String name, String age, Integer sex) {
        System.out.println("Student -> 全参构造方法");
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("InitializingBean接口中的afterPropertiesSet初始化方法");
    }

    public void initMethod() {
        System.out.println("自定义的普通方法作为初始化方法");
    }

    @Override
    public void destroy() {
        System.out.println("DisposableBean接口中的destroy销毁方法");
    }

    public void destroyMethod() {
        System.out.println("自定义的普通方法作为销毁方法");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("给 Student 类中的 name 属性赋值 " + name);
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        System.out.println("给 Student 类中的 age 属性赋值 " + age);
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        System.out.println("给 Student 类中的 sex 属性赋值 " + (sex == 0 ? "男" : "女"));
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student{" + "name='" + name + '\'' + ", age='" + age + '\'' + ", sex=" + sex + '}';
    }
}
