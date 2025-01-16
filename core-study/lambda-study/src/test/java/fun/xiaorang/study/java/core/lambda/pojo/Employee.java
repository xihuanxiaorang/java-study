package fun.xiaorang.study.java.core.lambda.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
  /**
   * 姓名
   */
  private String name;
  /**
   * 年龄
   */
  private Integer age;
  /**
   * 工资
   */
  private Double salary;
}
