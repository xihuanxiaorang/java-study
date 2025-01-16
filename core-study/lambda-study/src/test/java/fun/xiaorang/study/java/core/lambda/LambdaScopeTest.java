package fun.xiaorang.study.java.core.lambda;

import org.junit.jupiter.api.Test;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:30
 */
public class LambdaScopeTest {
  static int outerStaticNum;
  int outerNum;

  @Test
  public void test() {
    int num = 1;
    Converter<Integer, String> stringConverter = from -> String.valueOf(from + num);
    final String convert = stringConverter.convert(2);
    System.out.println(convert); // 3
  }

  @Test
  public void test2() {
    Converter<Integer, String> stringConverter1 = from -> {
      outerNum = 23;
      return String.valueOf(from + outerNum);
    };
    System.out.println(stringConverter1.convert(2)); // 25

    Converter<Integer, String> stringConverter2 = from -> {
      outerStaticNum = 72;
      return String.valueOf(from + outerStaticNum);
    };
    System.out.println(stringConverter2.convert(2)); // 74
  }

  @Test
  public void test3() {
    IFormula formula = new IFormula() {
      @Override
      public double calculate(final int a) {
        return sqrt(a * a);
      }
    };
    System.out.println(formula.calculate(4)); // 4
  }

  @FunctionalInterface
  interface Converter<F, T> {
    T convert(F from);
  }

  @FunctionalInterface
  interface IFormula {
    double calculate(int a);

    default double sqrt(int a) {
      return Math.sqrt(a);
    }
  }

}

