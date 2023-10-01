package fun.xiaorang.game._2048;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/1 14:52
 */
public class PlayerControl extends KeyAdapter {
    private final Box box;

    public PlayerControl(Box box) {
        this.box = box;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                this.box.moveUp();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                this.box.moveDown();
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                this.box.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                this.box.moveRight();
                break;
            default:
                break;
        }
    }
}
