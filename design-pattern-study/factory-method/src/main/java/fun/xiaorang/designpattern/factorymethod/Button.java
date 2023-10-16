package fun.xiaorang.designpattern.factorymethod;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/10 7:17
 */
public interface Button {
    /**
     * 渲染
     */
    void render();

    /**
     * 点击
     */
    void onClick();
}
