package fun.xiaorang.game.snake;

import javax.swing.*;
import java.awt.*;

import static fun.xiaorang.game.snake.Constants.GAME_WIDOWS_HEIGHT;
import static fun.xiaorang.game.snake.Constants.GAME_WIDOWS_WIDTH;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 20:55
 */
public class Game extends JFrame {
    public Game() throws HeadlessException {
        // 设置标题
        this.setTitle("贪吃蛇");
        // 获取屏幕的宽度和高度
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        // 游戏窗口居中显示
        this.setLocation((screenWidth - GAME_WIDOWS_WIDTH) >> 1, (screenHeight - GAME_WIDOWS_HEIGHT) >> 1);
        // 将窗口内容面板设置为自定义的主面板
        this.setContentPane(new MainPanel());
        // 自适应窗口大小
        this.pack();
        // 禁止窗口调整大小
        this.setResizable(false);
        // 窗口置顶
        this.setAlwaysOnTop(true);
        // 设置窗口关闭时的默认操作为退出程序
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.setVisible(true);
        });
    }
}
