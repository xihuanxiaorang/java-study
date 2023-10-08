package fun.xiaorang.game._2048;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/1 16:45
 */
public class GameData {
    private final Box box = new Box();
    private GameState gameState;

    public GameData() {
        // 初始化
        this.init();
    }

    public void init() {
        this.box.init();
        this.gameState = GameState.RUNNING;
    }

    public Box getBox() {
        return box;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
