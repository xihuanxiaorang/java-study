package fun.xiaorang.study.java.core.lambda;

import fun.xiaorang.study.java.core.lambda.pojo.Person;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:27
 */
public class MethodReferenceTest {
  @Test
  public void test() {
    final PrintStream ps = System.out;
    final Consumer<String> con1 = (s) -> ps.println(s);
    con1.accept("aaa");

    final Consumer<String> con2 = ps::println;
    con2.accept("bbb");
  }

  @Test
  public void test02() {
    Comparator<Integer> com1 = (x, y) -> Integer.compare(x, y);
    System.out.println(com1.compare(1, 2));

    Comparator<Integer> com2 = Integer::compare;
    System.out.println(com2.compare(2, 1));
  }

  @Test
  public void test03() {
    BiPredicate<String, String> bp1 = (x, y) -> x.equals(y);
    System.out.println(bp1.test("a", "b"));

    BiPredicate<String, String> bp2 = String::equals;
    System.out.println(bp2.test("c", "c"));
  }

  @Test
  public void test04() {
    final PersonFactory<Person> personFactory = Person::new;
    final Person person = personFactory.create("Peter", "Parker");
    System.out.println(person);
  }

  interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
  }
}

