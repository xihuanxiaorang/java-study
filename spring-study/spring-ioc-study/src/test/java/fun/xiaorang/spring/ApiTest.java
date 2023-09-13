package fun.xiaorang.spring;

import fun.xiaorang.spring.pojo.Customer;
import fun.xiaorang.spring.pojo.Person;
import fun.xiaorang.spring.pojo.Student;
import fun.xiaorang.spring.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/20 22:56
 */
public class ApiTest {
    private static ClassPathXmlApplicationContext ctx;

    @BeforeAll
    public static void before() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @AfterAll
    public static void after() {
        ctx.close();
    }

    @Test
    public void test_00() {
        Person person = ctx.getBean(Person.class);
        Person person1 = ctx.getBean(Person.class);
        System.out.println(person);
        System.out.println(person1);
        System.out.println(person == person1);
    }

    @Test
    public void test_01() {
        Person person = ctx.getBean("person", Person.class);
        System.out.println(person);
        // 没有指定类型，返回值需要进行强转
        Person person1 = (Person) ctx.getBean("person");
        System.out.println(person1);
        // 使用该方法时，在spring配置文件中只能有一个class是Person的bean，否则会报错
        Person person2 = ctx.getBean(Person.class);
        System.out.println(person2);
        // 获取spring配置文件中所有bean表标签的id
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        // 用于判断是否存在指定id或者name值的bean
        System.out.println(ctx.containsBean("person"));
        // 用于判断是否存在指定id值的bean
        System.out.println(ctx.containsBeanDefinition("person"));
    }

    @Test
    public void test_02() {
        Person person = ctx.getBean(Person.class);
        System.out.println(person);
    }

    @Test
    public void test_03() {
        Customer customer = ctx.getBean(Customer.class);
        System.out.println(customer);

        UserService userService = ctx.getBean(UserService.class);
        userService.save();
    }

    @Test
    public void test_04() {
        Connection connection = ctx.getBean(Connection.class);
        Connection connection1 = ctx.getBean(Connection.class);
        System.out.println(connection);
        System.out.println(connection1);
    }

    @Test
    public void test_05() {
        Student student = ctx.getBean(Student.class);
        System.out.println(student);
    }
}
