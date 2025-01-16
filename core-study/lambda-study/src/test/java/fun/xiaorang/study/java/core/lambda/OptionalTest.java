package fun.xiaorang.study.java.core.lambda;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:56
 */
public class OptionalTest {
  @Test
  public void test() {
    final Optional<Object> empty = Optional.empty();
    // System.out.println("empty.get() = " + empty.get()); // 抛出 NoSuchElementException 异常

    final Optional<Object> nullable = Optional.ofNullable(null);
    // System.out.println("nullable.get() = " + nullable.get());  // 抛出 NoSuchElementException 异常

    final Optional<String> non = Optional.of("test");
    System.out.println("non.get() = " + non.get()); // non.get() = test
  }

  @Test
  public void test2() {
    Object data1 = Optional.ofNullable(null).orElse("data");
    System.out.println(data1); // data
    Object data2 = Optional.of("data1").orElse("data2");
    System.out.println(data2); // data1
  }

  @Test
  public void test3() {
    final Object str = Optional.empty().orElseGet(() -> "Default Value");
    System.out.println(str); // Default Value

    final String str2 = Optional.of("Hello, World!").orElseGet(() -> "Default Value");
    System.out.println(str2); // Hello, World!
  }

  @Test
  public void test4() {
    final String res = Optional.of("Hello, World!").orElseThrow(() -> new IllegalArgumentException("Value is missing"));
    System.out.println(res); // Hello, World!

    // final Object res2 = Optional.empty().orElseThrow(() -> new IllegalArgumentException("Value is missing"));
    // System.out.println(res2); // 抛出 IllegalArgumentException 异常
  }

  @Test
  public void test5() {
    final boolean b1 = Optional.empty().isPresent(); // false
    final boolean b2 = Optional.ofNullable(null).isPresent(); // false
    final boolean b3 = Optional.of("Hello, World!").isPresent(); // true
  }

  @Test
  public void test6() {
    Optional.empty().ifPresent(System.out::println); // 不执行任何操作
    Optional.of("Hello, World!").ifPresent(System.out::println); // "Hello, World!"
  }

  @Test
  public void test7() {
    Predicate<Integer> predicate = num -> num > 40;
    Optional.of(42).filter(predicate).ifPresent(System.out::println); // 42
    Optional.of(30).filter(predicate).ifPresent(System.out::println); // 不执行任何操作
  }

  @Test
  public void test8() {
    Optional.of("hello").map(String::toUpperCase).ifPresent(System.out::println); // HELLO
    Optional.of(42).map(String::valueOf).ifPresent(System.out::println); // 42
  }

  @Test
  public void test9() {
    final Optional<String> optional1 = Optional.of("hello");
    final Optional<String> optional2 = Optional.of("world");
    optional1.flatMap(s1 -> optional2.map(s2 -> s1 + " " + s2)).ifPresent(System.out::println); // hello world
  }

}

