package fun.xiaorang.game._2048;

import java.awt.*;

import static fun.xiaorang.game._2048.Constants.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/1 14:25
 */
public class Box {
    private final Card[][] cards = new Card[ROWS][COLS];

    public Box() {
        // 初始化卡片
        this.initCards();
    }

    private void initCards() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; ++j) {
                cards[i][j] = new Card(i, j, 512);
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cards[i][j].draw(g);
            }
        }
    }

    private static class Card {
        private final int i;
        private final int j;
        private final int x;
        private final int y;
        private final int width = DEFAULT_CARD_WIDTH;
        private final int height = DEFAULT_CARD_HEIGHT;
        private final int num;

        public Card(int i, int j) {
            this(i, j, 0);
        }

        public Card(int i, int j, int num) {
            this.i = i;
            this.j = j;
            this.num = num;
            this.x = CARD_PANEL_PADDING + j * (DEFAULT_CARD_WIDTH + CARD_PADDING);
            this.y = CARD_PANEL_PADDING + i * (DEFAULT_CARD_HEIGHT + CARD_PADDING);
        }

        public void draw(Graphics g) {
            Color oldColor = g.getColor();
            this.drawCardBackground(g);
            this.drawNum(g);
            g.setColor(oldColor);
        }

        private void drawNum(Graphics g) {
            if (num == 0) {
                return;
            }
            String number = String.valueOf(this.num);
            g.setColor(this.getNumColor());
            g.setFont(DEFAULT_CARD_NUMBER_FONT);
            // 居中显示
            FontMetrics fontMetrics = g.getFontMetrics();
            int x = this.x + ((this.width - fontMetrics.stringWidth(number)) >> 1);
            // TODO 此处获得的字体高度比实际显示的高度要大，暂时不知道原因
            int y = this.y + ((this.height + fontMetrics.getHeight()) >> 1);
            // 绘制文字
            g.drawString(number, x, y);
        }

        private Color getNumColor() {
            if (num <= 4) {
                return CARD_NUMBER_COLOR_2_4;
            } else {
                return CARD_NUMBER_COLOR_OTHER;
            }
        }

        private void drawCardBackground(Graphics g) {
            g.setColor(this.getCarBackGroundColor());
            g.fillRoundRect(this.x, this.y, this.width, this.height, 10, 10);
        }

        private Color getCarBackGroundColor() {
            switch (num) {
                case 2:
                    return CARD_BACKGROUND_COLOR_2;
                case 4:
                    return CARD_BACKGROUND_COLOR_4;
                case 8:
                    return CARD_BACKGROUND_COLOR_8;
                case 16:
                    return CARD_BACKGROUND_COLOR_16;
                case 32:
                    return CARD_BACKGROUND_COLOR_32;
                case 64:
                    return CARD_BACKGROUND_COLOR_64;
                case 128:
                    return CARD_BACKGROUND_COLOR_128;
                case 256:
                    return CARD_BACKGROUND_COLOR_256;
                case 512:
                    return CARD_BACKGROUND_COLOR_512;
                case 1024:
                    return CARD_BACKGROUND_COLOR_1024;
                case 2048:
                    return CARD_BACKGROUND_COLOR_2048;
                default:
                    return DEFAULT_CARD_BACKGROUND_COLOR;
            }
        }
    }
}
