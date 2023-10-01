package fun.xiaorang.game._2048;

import java.awt.*;
import java.util.Random;

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
        // 初始化随机数字
        this.initRandomNum();
    }

    private void initRandomNum() {
        // 随机生成两个数字
        for (int i = 0; i < DEFAULT_GENERATE_CARD_NUM; i++) {
            this.generateRandomNum();
        }
    }

    private void generateRandomNum() {
        // 判断是否还有空位
        if (!this.hasEmptyCard()) {
            return;
        }
        // 随机生成一个数字，2或者4，其中2的概率为90%，4的概率为10%，并将数字填入随机的空白卡片中
        this.getRandomEmptyCard().num = new Random().nextInt(10) == 9 ? 4 : 2;
    }

    private Card getRandomEmptyCard() {
        Random random = new Random();
        // 随机生成一个坐标
        int i = random.nextInt(ROWS);
        int j = random.nextInt(COLS);
        Card card = cards[i][j];
        // 如果随机获取的卡片是空白卡片，则直接返回
        if (card.num == 0) {
            return card;
        }
        // 如果随机获取的卡片不是空白卡片，则重新递归获取
        return this.getRandomEmptyCard();
    }

    private boolean hasEmptyCard() {
        // 遍历所有卡片，如果有一个卡片的数字为0，就返回true
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; ++j) {
                if (cards[i][j].num == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initCards() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; ++j) {
                cards[i][j] = new Card(i, j);
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

    public void moveUp() {
        System.out.println("up");
    }

    public void moveDown() {
        System.out.println("down");
    }

    public void moveLeft() {
        System.out.println("left");
    }

    public void moveRight() {
        System.out.println("right");
    }

    private static class Card {
        private final int i;
        private final int j;
        private final int x;
        private final int y;
        private final int width = DEFAULT_CARD_WIDTH;
        private final int height = DEFAULT_CARD_HEIGHT;
        private int num;

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
