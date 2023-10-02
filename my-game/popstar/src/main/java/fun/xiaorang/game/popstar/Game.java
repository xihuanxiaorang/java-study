package fun.xiaorang.game.popstar;

import fun.xiaorang.game.popstar.scene.GameScene;
import fun.xiaorang.game.popstar.scene.Stage;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 20:19
 */
public class Game {
    public static void main(String[] args) {
        // 设置游戏场景
        Stage.getInstance().setScene(new GameScene());
    }
}
