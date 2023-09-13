package fun.xiaorang.springboot.annotation;

import fun.xiaorang.springboot.annotation.config.MainConfig;
import fun.xiaorang.springboot.annotation.controller.BookController;
import fun.xiaorang.springboot.annotation.controller.OrderController;
import fun.xiaorang.springboot.annotation.pojo.*;
import fun.xiaorang.springboot.annotation.repository.BookRepository;
import fun.xiaorang.springboot.annotation.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/12 17:02
 */
@SpringBootTest
public class ApiTest {
    @Test
    public void test_00(ApplicationContext applicationContext) {
        MainConfig mainConfig = applicationContext.getBean(MainConfig.class);
        Person person = applicationContext.getBean(Person.class);
        Pet pet = applicationContext.getBean(Pet.class);
        System.out.println(mainConfig);
        System.out.println(person);
        System.out.println(person.getPet());
        System.out.println(pet);
    }

    @Test
    public void test_01(ApplicationContext applicationContext) {
        BookController bookController = applicationContext.getBean(BookController.class);
        System.out.println(bookController);
        BookService bookService = applicationContext.getBean(BookService.class);
        System.out.println(bookService);
        BookRepository bookRepository = applicationContext.getBean(BookRepository.class);
        System.out.println(bookRepository);
    }

    @Test
    public void test_02(ApplicationContext applicationContext) {
        Man man = applicationContext.getBean(Man.class);
        System.out.println(man);
    }

    @Test
    public void test_03(ApplicationContext applicationContext) {
        Color color = applicationContext.getBean(Color.class);
        System.out.println(color);
        Yellow yellow = applicationContext.getBean(Yellow.class);
        System.out.println(yellow);
        Blue blue = applicationContext.getBean(Blue.class);
        System.out.println(blue);
        Rainbow rainbow = applicationContext.getBean(Rainbow.class);
        System.out.println(rainbow);
    }

    @Test
    public void test_04(ApplicationContext applicationContext) {
        Cat cat = applicationContext.getBean(Cat.class);
        System.out.println(cat);
    }

    @Test
    public void test_05(ApplicationContext applicationContext) {
        Teacher teacher = applicationContext.getBean(Teacher.class);
        System.out.println(teacher);
    }

    @Test
    public void test_06(ApplicationContext applicationContext) {
        OrderController orderController = applicationContext.getBean(OrderController.class);
        System.out.println(orderController);
    }
}
