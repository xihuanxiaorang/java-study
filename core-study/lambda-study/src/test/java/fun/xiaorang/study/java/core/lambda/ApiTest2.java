package fun.xiaorang.study.java.core.lambda;

import fun.xiaorang.study.java.core.lambda.pojo.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:18
 */
public class ApiTest2 {
  private final List<Employee> employees = new ArrayList<>();

  @BeforeEach
  public void before() {
    employees.add(new Employee("张三", 18, 9999.99));
    employees.add(new Employee("李四", 38, 5555.55));
    employees.add(new Employee("王五", 60, 6666.66));
    employees.add(new Employee("赵六", 16, 7777.77));
    employees.add(new Employee("田七", 18, 3333.33));
  }

  @Test
  public void test() {
    final List<Employee> employeeList = filterEmployeesByAge(employees);
    employeeList.forEach(System.out::println);
  }

  @Test
  public void test2() {
    final List<Employee> employeeList = filterEmployees(employees, new FilterEmployeeByAge());
    employeeList.forEach(System.out::println);
  }

  @Test
  public void test3() {
    final List<Employee> employeeList = filterEmployees(employees, new FilterEmployeeBySalary());
    employeeList.forEach(System.out::println);
  }

  @Test
  public void test4() {
    final List<Employee> employeeList = filterEmployees(employees, new MyPredicate<Employee>() {
      @Override
      public boolean filter(final Employee employee) {
        return employee.getAge() >= 30;
      }
    });
    employeeList.forEach(System.out::println);
  }

  @Test
  public void test5() {
    final List<Employee> employeeList = filterEmployees(employees, new MyPredicate<Employee>() {
      @Override
      public boolean filter(final Employee employee) {
        return employee.getSalary() >= 5000;
      }
    });
    employeeList.forEach(System.out::println);
  }

  @Test
  public void test6() {
    final List<Employee> employeeList = filterEmployees(employees, employee -> employee.getAge() >= 30);
    employeeList.forEach(System.out::println);
  }

  @Test
  public void test7() {
    final List<Employee> employeeList = filterEmployees(employees, employee -> employee.getSalary() >= 5000);
    employeeList.forEach(System.out::println);
  }


  public static List<Employee> filterEmployeesByAge(List<Employee> employees) {
    List<Employee> employeeList = new ArrayList<>();
    for (final Employee employee : employees) {
      if (employee.getAge() >= 30) {
        employeeList.add(employee);
      }
    }
    return employeeList;
  }

  public static List<Employee> filterEmployeesBySalary(List<Employee> employees) {
    List<Employee> employeeList = new ArrayList<>();
    for (final Employee employee : employees) {
      if (employee.getSalary() >= 5000) {
        employeeList.add(employee);
      }
    }
    return employeeList;
  }

  public static List<Employee> filterEmployees(List<Employee> employees, MyPredicate<Employee> myPredicate) {
    List<Employee> employeeList = new ArrayList<>();
    for (final Employee employee : employees) {
      if (myPredicate.filter(employee)) {
        employeeList.add(employee);
      }
    }
    return employeeList;
  }

  public interface MyPredicate<T> {
    boolean filter(T t);
  }

  public class FilterEmployeeByAge implements MyPredicate<Employee> {
    @Override
    public boolean filter(final Employee employee) {
      return employee.getAge() >= 30;
    }
  }

  public class FilterEmployeeBySalary implements MyPredicate<Employee> {
    @Override
    public boolean filter(final Employee employee) {
      return employee.getSalary() >= 5000;
    }
  }

}

