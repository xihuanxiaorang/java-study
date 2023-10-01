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
            for (int j = 0; j < COLS; j++) {
                if (cards[i][j].num == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initCards() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
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

    /**
     * 卡片向上移动，核心算法：
     * <p> 从上到下（第二行开始），从左到右遍历所有卡片：
     * <p> 1. 如果当前卡片是空白卡片，则不做任何操作；</p>
     * <p> 2. 如果当前卡片不是空白卡片，则判断当前卡片上方的卡片是否为空白卡片？</p>
     * <p> 2.1 如果为空白卡片，则直接将当前卡片移动到上方的卡片位置，即将当前卡片的数字赋值给上方卡片，当前卡片数字置为0；</p>
     * <p> 2.2 如果不为空白卡片，则判断当前卡片与上方卡片的数字是否相同并且上方卡片未被合并过？</p>
     * <p> 2.2.1 如果数字相同并且上方卡片未被合并过，则将当前卡片移动到上方卡片位置，即将当前卡片的数字赋值给上方卡片，当前卡片数字置为0，上方卡片数字翻倍，上方卡片标记为已合并；</p>
     * <p> 2.2.2 如果不相同，则不做任何操作；</p>
     * </p>
     */
    public void moveUp() {
        for (int i = 1; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // 向上移动当前卡片
                this.moveUp(cards[i][j]);
            }
        }
        // 重置所有卡片的合并标记
        this.resetMerged();
        // 生成一个随机数字
        this.generateRandomNum();
    }

    private void resetMerged() {
        // 遍历所有卡片，将所有卡片的合并标记置为false
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cards[i][j].merged = false;
            }
        }
    }

    private void moveUp(Card current) {
        // 判断当前卡片是否已经移动到第一行或者当前卡片是否为空白卡片
        if (current.i == 0 || current.num == 0) {
            return;
        }
        Card prev = cards[current.i - 1][current.j];
        // 判断当前卡片上方的卡片是否为空白卡片
        if (prev.num == 0) {
            // 如果为空白卡片，则直接将当前卡片移动到上方的卡片位置，即将当前卡片的数字赋值给上方卡片，当前卡片数字置为0
            prev.num = current.num;
            current.num = 0;
            // 递归移动上方的卡片
            this.moveUp(prev);
        } else {
            // 如果不为空白卡片，则判断当前卡片与上方卡片的数字是否相同并且上方卡片未被合并过
            if (prev.num == current.num && !prev.merged) {
                // 如果数字相同并且上方卡片未被合并过，则将当前卡片移动到上方卡片位置，即将当前卡片的数字赋值给上方卡片，当前卡片数字置为0，上方卡片数字翻倍，上方卡片标记为已合并
                prev.num <<= 1;
                current.num = 0;
                prev.merged = true;
            }
        }
    }

    /**
     * 卡片向下移动，核心算法：
     * <p> 从下到上（倒数第二行开始），从左到右遍历所有卡片：
     * <p> 1. 如果当前卡片是空白卡片，则不做任何操作；</p>
     * <p> 2. 如果当前卡片不是空白卡片，则判断当前卡片下方的卡片是否为空白卡片？</p>
     * <p> 2.1 如果为空白卡片，则直接将当前卡片移动到下方的卡片位置，即将当前卡片的数字赋值给下方卡片，当前卡片数字置为0；</p>
     * <p> 2.2 如果不为空白卡片，则判断当前卡片与下方卡片的数字是否相同并且下方卡片未被合并过？</p>
     * <p> 2.2.1 如果数字相同并且下方卡片未被合并过，则将当前卡片移动到下方卡片位置，即将当前卡片的数字赋值给下方卡片，当前卡片数字置为0，下方卡片数字翻倍，下方卡片标记为已合并；</p>
     * <p> 2.2.2 如果不相同，则不做任何操作；</p>
     * </p>
     */
    public void moveDown() {
        for (int i = ROWS - 2; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                // 向下移动当前卡片
                this.moveDown(cards[i][j]);
            }
        }
        // 重置所有卡片的合并标记
        this.resetMerged();
        // 生成一个随机数字
        this.generateRandomNum();
    }

    private void moveDown(Card card) {
        // 判断当前卡片是否已经移动到最后一行或者当前卡片是否为空白卡片
        if (card.i == ROWS - 1 || card.num == 0) {
            return;
        }
        Card next = cards[card.i + 1][card.j];
        // 判断当前卡片下方的卡片是否为空白卡片
        if (next.num == 0) {
            // 如果为空白卡片，则直接将当前卡片移动到下方的卡片位置，即将当前卡片的数字赋值给下方卡片，当前卡片数字置为0
            next.num = card.num;
            card.num = 0;
            // 递归移动下方的卡片
            this.moveDown(next);
        } else {
            // 如果不为空白卡片，则判断当前卡片与下方卡片的数字是否相同并且下方卡片未被合并过
            if (next.num == card.num && !next.merged) {
                // 如果数字相同并且下方卡片未被合并过，则将当前卡片移动到下方卡片位置，即将当前卡片的数字赋值给下方卡片，当前卡片数字置为0，下方卡片数字翻倍，下方卡片标记为已合并
                next.num <<= 1;
                card.num = 0;
                next.merged = true;
            }
        }
    }

    /**
     * 卡片向左移动，核心算法：
     * <p> 从左到右（第二列开始），从上到下遍历所有卡片：
     * <p> 1. 如果当前卡片是空白卡片，则不做任何操作；</p>
     * <p> 2. 如果当前卡片不是空白卡片，则判断当前卡片左方的卡片是否为空白卡片？</p>
     * <p> 2.1 如果为空白卡片，则直接将当前卡片移动到左方的卡片位置，即将当前卡片的数字赋值给左方卡片，当前卡片数字置为0；</p>
     * <p> 2.2 如果不为空白卡片，则判断当前卡片与左方卡片的数字是否相同并且左方卡片未被合并过？</p>
     * <p> 2.2.1 如果数字相同并且左方卡片未被合并过，则将当前卡片移动到左方卡片位置，即将当前卡片的数字赋值给左方卡片，当前卡片数字置为0，左方卡片数字翻倍，左方卡片标记为已合并；</p>
     * <p> 2.2.2 如果不相同，则不做任何操作；</p>
     * </p>
     */
    public void moveLeft() {
        for (int j = 1; j < COLS; j++) {
            for (int i = 0; i < ROWS; i++) {
                // 向左移动当前卡片
                this.moveLeft(cards[i][j]);
            }
        }
        // 重置所有卡片的合并标记
        this.resetMerged();
        // 生成一个随机数字
        this.generateRandomNum();
    }

    private void moveLeft(Card card) {
        // 判断当前卡片是否已经移动到第一列或者当前卡片是否为空白卡片
        if (card.j == 0 || card.num == 0) {
            return;
        }
        Card prev = cards[card.i][card.j - 1];
        // 判断当前卡片左方的卡片是否为空白卡片
        if (prev.num == 0) {
            // 如果为空白卡片，则直接将当前卡片移动到左方的卡片位置，即将当前卡片的数字赋值给左方卡片，当前卡片数字置为0
            prev.num = card.num;
            card.num = 0;
            // 递归移动左方的卡片
            this.moveLeft(prev);
        } else {
            // 如果不为空白卡片，则判断当前卡片与左方卡片的数字是否相同并且左方卡片未被合并过
            if (prev.num == card.num && !prev.merged) {
                // 如果数字相同并且左方卡片未被合并过，则将当前卡片移动到左方卡片位置，即将当前卡片的数字赋值给左方卡片，当前卡片数字置为0，左方卡片数字翻倍，左方卡片标记为已合并
                prev.num <<= 1;
                card.num = 0;
                prev.merged = true;
            }
        }
    }

    /**
     * 卡片向右移动，核心算法：
     * <p> 从右到左（倒数第二列开始），从上到下遍历所有卡片：
     * <p> 1. 如果当前卡片是空白卡片，则不做任何操作；</p>
     * <p> 2. 如果当前卡片不是空白卡片，则判断当前卡片右方的卡片是否为空白卡片？</p>
     * <p> 2.1 如果为空白卡片，则直接将当前卡片移动到右方的卡片位置，即将当前卡片的数字赋值给右方卡片，当前卡片数字置为0；</p>
     * <p> 2.2 如果不为空白卡片，则判断当前卡片与右方卡片的数字是否相同并且右方卡片未被合并过？</p>
     * <p> 2.2.1 如果数字相同并且右方卡片未被合并过，则将当前卡片移动到右方卡片位置，即将当前卡片的数字赋值给右方卡片，当前卡片数字置为0，右方卡片数字翻倍，右方卡片标记为已合并；</p>
     * <p> 2.2.2 如果不相同，则不做任何操作；</p>
     * </p>
     */
    public void moveRight() {
        for (int j = COLS - 2; j >= 0; j--) {
            for (int i = 0; i < ROWS; i++) {
                // 向右移动当前卡片
                this.moveRight(cards[i][j]);
            }
        }
        // 重置所有卡片的合并标记
        this.resetMerged();
        // 生成一个随机数字
        this.generateRandomNum();
    }

    private void moveRight(Card card) {
        // 判断当前卡片是否已经移动到最后一列或者当前卡片是否为空白卡片
        if (card.j == COLS - 1 || card.num == 0) {
            return;
        }
        Card next = cards[card.i][card.j + 1];
        // 判断当前卡片右方的卡片是否为空白卡片
        if (next.num == 0) {
            // 如果为空白卡片，则直接将当前卡片移动到右方的卡片位置，即将当前卡片的数字赋值给右方卡片，当前卡片数字置为0
            next.num = card.num;
            card.num = 0;
            // 递归移动右方的卡片
            this.moveRight(next);
        } else {
            // 如果不为空白卡片，则判断当前卡片与右方卡片的数字是否相同并且右方卡片未被合并过
            if (next.num == card.num && !next.merged) {
                // 如果数字相同并且右方卡片未被合并过，则将当前卡片移动到右方卡片位置，即将当前卡片的数字赋值给右方卡片，当前卡片数字置为0，右方卡片数字翻倍，右方卡片标记为已合并
                next.num <<= 1;
                card.num = 0;
                next.merged = true;
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
        private int num;
        /**
         * 合并标记
         */
        private boolean merged;

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
