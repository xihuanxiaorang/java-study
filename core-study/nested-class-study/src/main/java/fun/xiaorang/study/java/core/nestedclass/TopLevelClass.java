package fun.xiaorang.study.java.core.nestedclass;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/15 22:54
 */
public class TopLevelClass {
  void accessMembers(OuterClass outer) {
    // Compiler error: Cannot make a static reference to the non-static field OuterClass.outerField
    // System.out.println(OuterClass.outerField);
    System.out.println(outer.outerField);
    System.out.println(OuterClass.staticOuterField);
  }
}
