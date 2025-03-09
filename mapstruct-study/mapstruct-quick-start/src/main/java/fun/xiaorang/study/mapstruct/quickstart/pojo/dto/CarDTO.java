package fun.xiaorang.study.mapstruct.quickstart.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/03/01 18:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {
  private String make;
  private int seatCount;
  private String type;
}
