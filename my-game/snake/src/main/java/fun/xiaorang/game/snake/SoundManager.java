package fun.xiaorang.game.snake;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/28 13:50
 */
public class SoundManager {
    private static Clip bgm;
    private static Clip gameOver;
    private static Clip eatFood;

    static {
        loadBackgroundMusic();
        loadGameOverMusic();
        loadEatFoodMusic();
    }

    private static void loadEatFoodMusic() {
        try (InputStream is = SoundManager.class.getResourceAsStream("/sounds/eatfood.wav");
             AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(is))) {
            eatFood = AudioSystem.getClip();
            eatFood.open(ais);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadGameOverMusic() {
        try (InputStream is = SoundManager.class.getResourceAsStream("/sounds/gameover.wav");
             AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(is))) {
            gameOver = AudioSystem.getClip();
            gameOver.open(ais);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadBackgroundMusic() {
        try (InputStream is = SoundManager.class.getResourceAsStream("/sounds/bgm.wav");
             AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(is))) {
            bgm = AudioSystem.getClip();
            bgm.open(ais);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void playEatFoodMusic() {
        if (eatFood != null) {
            eatFood.stop(); // 停止之前的播放（可选）
            eatFood.setFramePosition(0); // 重置音效位置
            eatFood.start(); // 播放音效
        }
    }

    public static void playGameOverMusic() {
        if (gameOver != null) {
            gameOver.stop(); // 停止之前的播放（可选）
            gameOver.setFramePosition(0); // 重置音效位置
            gameOver.start(); // 播放音效
        }
    }

    public static void stopGameOverMusic() {
        if (gameOver != null && gameOver.isRunning()) {
            // 停止播放
            gameOver.stop();
        }
    }

    public static void playBackgroundMusic() {
        if (bgm != null && !bgm.isRunning()) {
            // 循环播放
            bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopBackgroundMusic() {
        if (bgm != null && bgm.isRunning()) {
            // 停止播放
            bgm.stop();
        }
    }
}


