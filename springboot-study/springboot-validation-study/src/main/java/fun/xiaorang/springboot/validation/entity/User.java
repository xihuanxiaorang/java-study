package fun.xiaorang.springboot.validation.entity;

import fun.xiaorang.springboot.validation.validation.Update;
import fun.xiaorang.springboot.validation.validation.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/13 23:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Long id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度在{min}-{max}之间")
    private String password;
    @Min(value = 0, message = "年龄最小为{value}")
    @Max(value = 200, message = "年龄最大为{value}")
    private Integer age;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotNull(message = "地址不能为空")
    @Valid
    private Address address;
    @UserStatus
    private Integer status;
}
