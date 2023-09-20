package fun.xiaorang.mybatis.mapper;

import fun.xiaorang.mybatis.entity.Author;
import org.apache.ibatis.annotations.Param;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/20 16:07
 */
public interface AuthorMapper {
    Author findOne(@Param("id") int id);
}