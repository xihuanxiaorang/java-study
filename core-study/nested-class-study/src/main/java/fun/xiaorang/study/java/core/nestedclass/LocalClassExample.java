package fun.xiaorang.study.java.core.nestedclass;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/15 22:55
 */
public class LocalClassExample {

  static String regularExpression = "[^0-9]";

  public static void validatePhoneNumber(String phoneNumber1, String phoneNumber2) {

    final int numberLength = 10;

    // Valid in JDK 8 and later:

    // int numberLength = 10;

    class PhoneNumber {

      String formattedPhoneNumber = null;

      PhoneNumber(String phoneNumber) {
        // numberLength = 7;
        String currentNumber = phoneNumber.replaceAll(regularExpression, "");
        if (currentNumber.length() == numberLength)
          formattedPhoneNumber = currentNumber;
        else
          formattedPhoneNumber = null;
      }

      public String getNumber() {
        return formattedPhoneNumber;
      }

      // Valid in JDK 8 and later:

      //            public void printOriginalNumbers() {
      //                System.out.println("Original numbers are " + phoneNumber1 + " and " + phoneNumber2);
      //            }
    }

    PhoneNumber myNumber1 = new PhoneNumber(phoneNumber1);
    PhoneNumber myNumber2 = new PhoneNumber(phoneNumber2);

    // Valid in JDK 8 and later:

    //        myNumber1.printOriginalNumbers();

    if (myNumber1.getNumber() == null)
      System.out.println("First number is invalid");
    else
      System.out.println("First number is " + myNumber1.getNumber());
    if (myNumber2.getNumber() == null)
      System.out.println("Second number is invalid");
    else
      System.out.println("Second number is " + myNumber2.getNumber());

  }

  public static void main(String... args) {
    validatePhoneNumber("123-456-7890", "456-7890");
  }
}
