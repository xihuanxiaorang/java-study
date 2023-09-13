package fun.xiaorang.spring.pojo;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/4/20 22:49
 */
public class Person {
    private String name;
    private Integer age;
    private String address;

    private Person() {
        System.out.println("Person -> 无参构造函数");
    }

    public Person(String name) {
        System.out.println("Person -> 只有name参数的构造函数");
        this.name = name;
    }

    public Person(String name, Integer age, String address) {
        System.out.println("Person -> 全参构造函数");
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Person(String name, String address, Integer age) {
        System.out.println("Person -> 全参构造函数2");
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("给 Person 中的 name 属性赋值 " + name);
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        System.out.println("给 Person 中的 age 属性赋值 " + age);
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        System.out.println("给 Person 中的 address 属性赋值 " + address);
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", age=" + age + ", address='" + address + '\'' + '}';
    }
}
