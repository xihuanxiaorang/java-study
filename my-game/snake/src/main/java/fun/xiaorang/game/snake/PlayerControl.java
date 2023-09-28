package fun.xiaorang.game.snake;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static fun.xiaorang.game.snake.Constants.KEY_INTERVAL;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 23:24
 */
public class PlayerControl extends KeyAdapter implements ActionListener {
    private final GamePanel gamePanel;
    /**
     * 是否可以改变方向
     */
    private boolean canChangeDirection = true;

    public PlayerControl(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        // 定时器，用于重新设置是否可以改变方向，防止用户在一次按键事件中多次改变方向
        Timer timer = new Timer(KEY_INTERVAL, this);
        // 启动定时器
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        changeState(e);
        if (this.gamePanel.getState() != State.RUNNING) {
            return;
        }
        changeDirection(e);
    }

    private void changeState(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.gamePanel.changeState();
        }
    }

    private void changeDirection(KeyEvent e) {
        if (!canChangeDirection) {
            return;
        }
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
            Snake snake = this.gamePanel.getSnake();
            Direction currentDirection = snake.getDirection();
            if (currentDirection == Direction.UP && direction == Direction.DOWN
                    || currentDirection == Direction.DOWN && direction == Direction.UP
                    || currentDirection == Direction.LEFT && direction == Direction.RIGHT
                    || currentDirection == Direction.RIGHT && direction == Direction.LEFT) {
                return;
            }
            snake.changeDirection(direction);
            canChangeDirection = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 设置为可以改变方向
        canChangeDirection = true;
    }
}
