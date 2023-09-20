package fun.xiaorang.mybatis.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/20 16:07
 */
@Getter
public enum ArticleTypeEnum {
    JAVA(1),
    DUBBO(2),
    SPRING(4),
    MYBATIS(8);

    private final int code;

    ArticleTypeEnum(int code) {
        this.code = code;
    }

    /**
     * 根据code获取枚举
     *
     * @param code code
     * @return 枚举
     */
    public static ArticleTypeEnum getEnumByCode(int code) {
        return Arrays.stream(ArticleTypeEnum.values())
                .filter(articleTypeEnum -> articleTypeEnum.code == code)
                .findFirst()
                .orElse(null);
    }
}