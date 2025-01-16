package fun.xiaorang.study.java.core.lambda;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:14
 */
public class ApiTest {
  @Test
  public void test() {
    final Comparator<Integer> comparator = new Comparator<Integer>() {
      @Override
      public int compare(final Integer o1, final Integer o2) {
        return Integer.compare(o1, o2);
      }
    };
    final TreeSet<Integer> treeSet = new TreeSet<>(comparator);
    treeSet.addAll(Arrays.asList(1, 3, 2, 4, 6, 5));
    treeSet.forEach(System.out::println);
  }
}

