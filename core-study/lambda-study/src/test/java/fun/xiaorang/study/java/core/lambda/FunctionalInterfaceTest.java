package fun.xiaorang.study.java.core.lambda;

import fun.xiaorang.study.java.core.lambda.pojo.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:32
 */
@Slf4j
public class FunctionalInterfaceTest {
  @Test
  public void test() {
    Predicate<String> predicate = (s) -> s.length() > 0;
    String str = "foo";
    log.info("字符串 {} 的长度是否大于0？{}", str, predicate.test(str));
    log.info("字符串 {} 的长度是否小于等于0？{}", str, predicate.negate().test(str));

    Predicate<Boolean> nonNull = Objects::nonNull;
    Predicate<Boolean> isNull = Objects::isNull;
    Boolean b = true;
    log.info("布尔类型的 {} 是否不为null？{}", b, nonNull.test(b));
    log.info("布尔类型的 {} 是否为null？{}", b, isNull.test(b));

    Predicate<String> isEmpty = String::isEmpty;
    Predicate<String> isNotEmpty = isEmpty.negate();
    log.info("字符串 {} 是否为空？{}", str, isEmpty.test(str));
    log.info("字符串 {} 是否不为空？{}", str, isNotEmpty.test(str));
  }

  @Test
  public void test2() {
    // 需求：先将字符串反转之后取第一个字符再转换成数字 "123" => 3
    final Function<String, Integer> toIntegerFunction = Integer::valueOf;
    final Function<String, String> reserveFunction = this::reserve;
    final Function<String, String> startsWithFunction = new Something()::startsWith;
    String str = "123";

    // andThen 实现
    final Function<String, Integer> function = reserveFunction.andThen(startsWithFunction).andThen(toIntegerFunction);
    log.info("字符串 {} 反转之后取第一个字符再转换成数字是 {}", str, function.apply(str));

    // compose 实现
    final Function<String, Integer> function1 = toIntegerFunction.compose(startsWithFunction).compose(reserveFunction);
    log.info("字符串 {} 反转之后取第一个字符再转换成数字是 {}", str, function1.apply(str));
  }

  public String reserve(String str) {
    int length = str.length();
    if (str.isEmpty() || length == 1) {
      return str;
    }
    final String left = str.substring(0, length / 2);
    final String right = str.substring(length / 2, length);
    return reserve(right) + reserve(left);
  }

  @Test
  public void test3() {
    final Supplier<Person> personSupplier = Person::new;
    log.info("调用 Person 类的无参构造方法创建一个 Person 对象实例：{}", personSupplier.get());
  }

  @Test
  public void test4() {
    Consumer<Person> greeter = person -> log.info("Hello, {}", person.getFirstName());
    greeter.accept(new Person("Peter", "Parker"));
  }

  class Something {
    String startsWith(String s) {
      return String.valueOf(s.charAt(0));
    }
  }

}

