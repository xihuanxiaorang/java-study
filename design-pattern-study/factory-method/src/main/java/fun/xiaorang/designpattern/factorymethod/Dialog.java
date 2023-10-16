package fun.xiaorang.designpattern.factorymethod;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/10 7:38
 */
public abstract class Dialog {
    /**
     * 渲染
     */
    public void render() {
        Button okButton = this.createButton();
        okButton.render();
    }

    /**
     * 创建按钮
     *
     * @return Button
     */
    public abstract Button createButton();
}
