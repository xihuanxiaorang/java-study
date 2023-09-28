package fun.xiaorang.game.snake;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 23:24
 */
public class PlayerControl extends KeyAdapter {
    private final GamePanel gamePanel;

    public PlayerControl(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        changeDirection(e);
        changeState(e);
    }

    private void changeState(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.gamePanel.changeState();
        }
    }

    private void changeDirection(KeyEvent e) {
        Direction direction = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                direction = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                direction = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                direction = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                direction = Direction.RIGHT;
                break;
            default:
                break;
        }
        if (direction != null) {
            this.gamePanel.changeDirection(direction);
        }
    }
}
