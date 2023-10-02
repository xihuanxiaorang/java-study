package fun.xiaorang.game.popstar.scene;

import fun.xiaorang.game.popstar.manager.ImageManager;

import javax.swing.*;
import java.awt.*;

import static fun.xiaorang.game.popstar.core.Constants.GAME_SCENE_HEIGHT;
import static fun.xiaorang.game.popstar.core.Constants.GAME_SCENE_WIDTH;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 20:29
 */
public class GameScene extends JPanel {
    public GameScene() {
        // 取消默认布局，使用自由布局
        this.setLayout(null);
        // 设置首选大小，以便窗口能够自适应内容面板大小
        this.setPreferredSize(new Dimension(GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT));
        // 添加游戏面板
        this.add(new GamePanel());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图片
        g.drawImage(ImageManager.BACKGROUND, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
