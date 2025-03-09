package fun.xiaorang.study.mapstruct.quickstart.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/03/01 18:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
  private String make;
  private int numberOfSeats;
  private CarType type;

  public enum CarType {
    SEDAN, HATCHBACK, SUV
  }
}
