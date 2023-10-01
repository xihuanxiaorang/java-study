package fun.xiaorang.game._2048;

import javax.swing.*;
import java.awt.*;

import static fun.xiaorang.game._2048.Constants.GAME_WIDOWS_HEIGHT;
import static fun.xiaorang.game._2048.Constants.GAME_WIDOWS_WIDTH;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/1 13:52
 */
public class MainPanel extends JPanel {
    public MainPanel() {
        // 设置当前面板为自由布局
        this.setLayout(null);
        // 设置面板大小，让游戏窗口自适应
        this.setPreferredSize(new Dimension(GAME_WIDOWS_WIDTH, GAME_WIDOWS_HEIGHT));
        // 添加面板
        this.addPanels();
    }

    private void addPanels() {
        // 添加游戏面板
        this.add(new GamePanel());
    }
}
