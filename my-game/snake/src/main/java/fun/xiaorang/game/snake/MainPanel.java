package fun.xiaorang.game.snake;

import javax.swing.*;
import java.awt.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/28 12:19
 */
public class MainPanel extends JPanel {
    public MainPanel() {
        // 设置当前面板为自由布局
        this.setLayout(null);
        // 设置面板大小，让游戏窗口自适应
        this.setPreferredSize(new Dimension(Constants.GAME_WIDOWS_WIDTH, Constants.GAME_WIDOWS_HEIGHT));
        // 初始化图层
        this.initLayers();
    }

    private void initLayers() {
        // 添加标题面板
        this.add(new TitlePanel());
        // 添加游戏面板
        this.add(new GamePanel());
    }
}
