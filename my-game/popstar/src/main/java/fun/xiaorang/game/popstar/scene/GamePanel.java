package fun.xiaorang.game.popstar.scene;

import fun.xiaorang.game.popstar.core.Star;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fun.xiaorang.game.popstar.core.Constants.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/2 21:14
 */
public class GamePanel extends JPanel implements ActionListener {
    private static final Star[][] STARS = new Star[ROWS][COLS];

    public GamePanel() {
        // 初始化
        this.init();
        // 初始化星星
        this.initStars();
    }

    private void initStars() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                STARS[i][j] = new Star(i, j);
            }
        }
    }

    private void init() {
        // 取消默认布局，使用自由布局
        this.setLayout(null);
        // 设置面板大小以及位置
        this.setBounds(0, 0, GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT);
        // 设置面板透明
        this.setOpaque(false);
        // 添加鼠标监听
        this.addMouseListener(new PlayerControl());
        // 将当前面板标记为可获得焦点的组件
        this.setFocusable(true);
        // 请求当前面板在窗口中获得焦点
        this.requestFocusInWindow();
        // 通过请求焦点来确保当前面板获得焦点（这行通常不会影响焦点的设置，但可以作为一种额外的措施）
        this.requestFocus();
        // 创建定时器，用于刷新面板，实现动画效果，每秒刷新60次
        Timer timer = new Timer(1000 / DEFAULT_FRAME_RATE, this);
        // 启动定时器
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制星星
        this.drawStars(g);
    }

    private void drawStars(Graphics g) {
        // 遍历所有星星
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Star star = STARS[i][j];
                if (star != null) {
                    // 绘制星星
                    star.draw(g);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 重绘面板
        this.repaint();
    }

    private static class PlayerControl extends MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            // 获取鼠标点击的星星
            Star star = getStarByMouse(e.getX(), e.getY());
            // 判断鼠标点击的星星是否为空
            if (star == null) {
                return;
            }
            // 判断鼠标点击的星星是否被选中
            if (star.isSelected()) {
                // 消除选中的星星
                popSelectedStars();
                // 星星下落
                starsFall();
            } else {
                // 清除所有选中的星星
                clearSelectedStars();
                // 选中当前星星周围颜色相同的星星
                selectSameColorStars(star);
            }
        }

        /**
         * 星星下落
         */
        private void starsFall() {
            // 首先获取所有不为空的星星，然后重新排列所有星星，实现星星下落效果
            rearrangeStars(getStarsNotEqualsNull());
        }

        /**
         * 重新排列所有星星，实现星星下落效果
         *
         * @param map 所有不为空的星星
         */
        private void rearrangeStars(Map<Integer, List<Star>> map) {
            for (int j = 0; j < COLS; j++) {
                List<Star> stars = map.get(j);
                int row = 0;
                for (int i = ROWS - 1; i >= 0; i--) {
                    if (stars == null || stars.isEmpty() || row >= stars.size()) {
                        STARS[i][j] = null;
                        continue;
                    }
                    Star star = stars.get(row++);
                    STARS[i][j] = new Star(i, j, star.getColor());
                }
            }
        }

        /**
         * 按从左到右，从下到上收集所有不为空的星星，将其放到一个 Map<Integer, List<Star>> 集合中，其中 key 为列索引，value 为该列中所有不为空的星星
         *
         * @return Map<Integer, List < Star>> 集合
         */
        private Map<Integer, List<Star>> getStarsNotEqualsNull() {
            Map<Integer, List<Star>> map = new HashMap<>();
            int colKey = 0;
            // 遍历所有星星
            for (int j = 0; j < COLS; j++) {
                List<Star> stars = new ArrayList<>();
                for (int i = ROWS - 1; i >= 0; i--) {
                    Star star = STARS[i][j];
                    if (star == null) {
                        continue;
                    }
                    stars.add(star);
                }
                if (stars.isEmpty()) {
                    continue;
                }
                map.put(colKey++, stars);
            }
            return map;
        }

        /**
         * 消除选中的星星
         */
        private void popSelectedStars() {
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    if (STARS[i][j] != null && STARS[i][j].isSelected()) {
                        STARS[i][j] = null;
                    }
                }
            }
        }

        /**
         * 选中当前星星周围颜色相同的星星
         *
         * @param star 当前星星
         */
        private void selectSameColorStars(Star star) {
            // 判断当前星星是否为空或者已经被选中，如果是则直接返回
            if (star == null || star.isSelected()) {
                return;
            }
            // 选中当前星星
            star.setSelected(true);
            // 获取当前星星的行列索引
            int row = star.getRow();
            int col = star.getCol();
            Star up, down, left, right;
            // 选中当前星星上方的星星
            if (row > 0 && (up = STARS[row - 1][col]) != null && up.getColor() == star.getColor()) {
                selectSameColorStars(up);
            }
            // 选中当前星星下方的星星
            if (row < ROWS - 1 && (down = STARS[row + 1][col]) != null && down.getColor() == star.getColor()) {
                selectSameColorStars(down);
            }
            // 选中当前星星左方的星星
            if (col > 0 && (left = STARS[row][col - 1]) != null && left.getColor() == star.getColor()) {
                selectSameColorStars(left);
            }
            // 选中当前星星右方的星星
            if (col < COLS - 1 && (right = STARS[row][col + 1]) != null && right.getColor() == star.getColor()) {
                selectSameColorStars(right);
            }
        }

        /**
         * 清除所有选中的星星
         */
        private void clearSelectedStars() {
            // 遍历所有星星
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    Star star = STARS[i][j];
                    if (star != null) {
                        // 清除选中状态
                        star.setSelected(false);
                    }
                }
            }
        }

        /**
         * 根据鼠标点击的坐标获取对应的星星
         *
         * @param x 鼠标点击的x坐标
         * @param y 鼠标点击的y坐标
         * @return 星星
         */
        private Star getStarByMouse(int x, int y) {
            // 计算鼠标点击的星星的行列索引
            int row = y / STAR_HEIGHT;
            int col = x / STAR_WIDTH;
            // 获取鼠标点击的星星
            return STARS[row][col];
        }
    }
}
