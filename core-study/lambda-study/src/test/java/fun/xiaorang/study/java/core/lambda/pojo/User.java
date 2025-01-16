package fun.xiaorang.study.java.core.lambda.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Comparable<User> {
  /**
   * 用户ID
   */
  private Integer id;
  /**
   * 用户名
   */
  private String name;
  /**
   * 年龄
   */
  private Integer age;
  /**
   * 密码
   */
  private String password;
  /**
   * 邮箱
   */
  private String email;

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(name, user.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public int compareTo(final User o) {
    return o.getAge() - this.getAge();
  }
}



