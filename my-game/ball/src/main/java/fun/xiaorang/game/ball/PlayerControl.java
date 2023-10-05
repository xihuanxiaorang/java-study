package fun.xiaorang.game.ball;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static fun.xiaorang.game.ball.Constants.DEFAULT_RACKET_SPEED;

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

    @Override
    public void keyReleased(KeyEvent e) {
        Racket racket = this.gamePanel.getRacket();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                racket.setSpeed(0);
                break;
            default:
                break;
        }
    }

    private void changeDirection(KeyEvent e) {
        Racket racket = this.gamePanel.getRacket();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                racket.setSpeed(-DEFAULT_RACKET_SPEED);
                break;
            case KeyEvent.VK_RIGHT:
                racket.setSpeed(DEFAULT_RACKET_SPEED);
                break;
            default:
                break;
        }
    }
}
