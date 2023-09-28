package fun.xiaorang.game.snake;

import javax.swing.*;
import java.awt.*;

import static fun.xiaorang.game.snake.Constants.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/28 12:20
 */
public class TitlePanel extends JPanel {
    public TitlePanel() {
        this.setBounds(PADDING, PADDING, TITLE_WIDTH, TITLE_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Img.TITLE != null) {
            // 居中显示
            int x = (this.getWidth() - Img.TITLE.getWidth(null)) >> 1;
            int y = (this.getHeight() - Img.TITLE.getHeight(null)) >> 1;
            // 绘制标题
            g.drawImage(Img.TITLE, x, y, null);
        }
    }
}
