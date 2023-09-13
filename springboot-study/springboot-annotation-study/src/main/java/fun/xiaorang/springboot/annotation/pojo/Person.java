package fun.xiaorang.springboot.annotation.pojo;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">
 * <p>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a> @Copyright
 * 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a> - show me the code
 * @date 2023/5/12 16:59
 */
public class Person {
    private String name;
    private Integer age;
    private Pet pet;

    public Person() {
    }

    public Person(String name, Integer age, Pet pet) {
        this.name = name;
        this.age = age;
        this.pet = pet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
