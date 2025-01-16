package fun.xiaorang.study.java.core.nestedclass;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/15 22:55
 */
public class HelloWorldAnonymousClasses {

  public void sayHello() {

    class EnglishGreeting implements HelloWorld {
      String name = "world";

      @Override
      public void greet() {
        greetSomeone("world");
      }

      @Override
      public void greetSomeone(String someone) {
        name = someone;
        System.out.println("Hello " + name);
      }
    }

    HelloWorld englishGreeting = new EnglishGreeting();

    HelloWorld frenchGreeting = new HelloWorld() {
      String name = "tout le monde";

      @Override
      public void greet() {
        greetSomeone("tout le monde");
      }

      @Override
      public void greetSomeone(String someone) {
        name = someone;
        System.out.println("Salut " + name);
      }
    };

    HelloWorld spanishGreeting = new HelloWorld() {
      String name = "mundo";

      @Override
      public void greet() {
        greetSomeone("mundo");
      }

      @Override
      public void greetSomeone(String someone) {
        name = someone;
        System.out.println("Hola, " + name);
      }
    };

    englishGreeting.greet();
    frenchGreeting.greetSomeone("Fred");
    spanishGreeting.greet();
  }

  public static void main(String... args) {
    HelloWorldAnonymousClasses myApp = new HelloWorldAnonymousClasses();
    myApp.sayHello();
  }

  interface HelloWorld {
    void greet();

    void greetSomeone(String someone);
  }
}
