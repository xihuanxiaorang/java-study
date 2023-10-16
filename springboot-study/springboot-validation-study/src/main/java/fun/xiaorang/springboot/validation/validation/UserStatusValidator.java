package fun.xiaorang.springboot.validation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/14 21:10
 */
public class UserStatusValidator implements ConstraintValidator<UserStatus, Integer> {
    @Override
    public boolean isValid(Integer status, ConstraintValidatorContext constraintValidatorContext) {
        if (status == null) {
            return false;
        }
        return Arrays.asList(1, 2, 3).contains(status);
    }
}
