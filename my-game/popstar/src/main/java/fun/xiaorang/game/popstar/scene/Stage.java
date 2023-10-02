package fun.xiaorang.game.popstar.scene;

import javax.swing.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 20:23
 */
public class Stage extends JFrame {
    private static final Stage INSTANCE = new Stage();

    private Stage() {
        // 设置窗口标题
        this.setTitle("消灭星星");
        // 设置窗口不可调整大小
        this.setResizable(false);
        // 设置窗口可见
        this.setVisible(true);
        // 设置窗口关闭时退出程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static Stage getInstance() {
        return INSTANCE;
    }

    /**
     * 设置场景，切换窗口内容面板，实现场景切换
     *
     * @param panel 窗口内容面板，即场景
     */
    public void setScene(JPanel panel) {
        // 设置窗口内容面板
        this.setContentPane(panel);
        // 设置窗口自适应内容面板大小
        this.pack();
        // 设置窗口居中
        this.setLocationRelativeTo(null);
    }
}
