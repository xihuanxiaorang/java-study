package fun.xiaorang.datastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/8/24 17:49
 */
public class ArrayListTest {
    @Test
    public void test() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(1, 2);
        System.out.println(list);
        Assertions.assertEquals(1, list.get(0));
        Assertions.assertEquals(2, list.get(1));
        Assertions.assertEquals(3, list.get(2));
        list.set(0, 4);
        Assertions.assertEquals(4, list.get(0));
        list.clear();
        System.out.println(list);
    }
}
