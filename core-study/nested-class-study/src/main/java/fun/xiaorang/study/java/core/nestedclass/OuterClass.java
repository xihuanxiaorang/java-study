package fun.xiaorang.study.java.core.nestedclass;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/15 22:53
 */
public class OuterClass {
  static String staticOuterField = "Static outer field";
  String outerField = "Outer field";

  public static void main(String[] args) {
    System.out.println("Inner class:");
    System.out.println("------------");
    OuterClass outerObject = new OuterClass();
    OuterClass.InnerClass innerObject = outerObject.new InnerClass();
    innerObject.accessMembers();

    System.out.println("\nStatic nested class:");
    System.out.println("--------------------");
    StaticNestedClass staticNestedObject = new StaticNestedClass();
    staticNestedObject.accessMembers(outerObject);

    System.out.println("\nTop-level class:");
    System.out.println("--------------------");
    TopLevelClass topLevelObject = new TopLevelClass();
    topLevelObject.accessMembers(outerObject);
  }

  static class StaticNestedClass {
    void accessMembers(OuterClass outer) {
      // Compiler error: Cannot make a static reference to the non-static field outerField
      // System.out.println(outerField);
      System.out.println(outer.outerField);
      System.out.println(staticOuterField);
    }
  }

  class InnerClass {
    void accessMembers() {
      System.out.println(outerField);
      System.out.println(staticOuterField);
    }
  }
}
