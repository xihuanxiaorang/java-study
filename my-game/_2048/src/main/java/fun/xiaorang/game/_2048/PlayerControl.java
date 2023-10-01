package fun.xiaorang.game._2048;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static fun.xiaorang.game._2048.GameState.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/1 14:52
 */
public class PlayerControl extends KeyAdapter {
    private final GameData gameData;

    public PlayerControl(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 改变游戏状态
        this.changeGameState(e);
        if (this.gameData.getGameState() != RUNNING) {
            return;
        }
        // 移动卡片
        this.moveCard(e);
    }

    private void changeGameState(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            GameState gameState = this.gameData.getGameState();
            if (gameState == PAUSE) {
                this.gameData.setGameState(RUNNING);
            } else if (gameState == RUNNING) {
                this.gameData.setGameState(PAUSE);
            } else if (gameState == OVER || gameState == WIN) {
                this.gameData.init();
            }
        }
    }

    private void moveCard(KeyEvent e) {
        Box box = this.gameData.getBox();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                box.moveUp();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                box.moveDown();
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                box.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                box.moveRight();
                break;
            default:
                break;
        }
    }
}
