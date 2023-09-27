package fun.xiaorang.game.snake;

import java.awt.*;
import java.util.LinkedList;

import static fun.xiaorang.game.snake.Constants.SNAKE_NODE_HEIGHT;
import static fun.xiaorang.game.snake.Constants.SNAKE_NODE_WIDTH;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/9/27 21:27
 */
public class Snake {
    /**
     * 蛇的全部节点
     */
    private final LinkedList<Node> nodes = new LinkedList<>();

    public Snake() {
        this.nodes.add(Node.builder(new Point(3, 2)).head(Direction.RIGHT).build());
        this.nodes.add(Node.builder(new Point(2, 2)).body().build());
        this.nodes.add(Node.builder(new Point(1, 2)).body().build());
    }

    public void draw(Graphics g, Food food) {
        this.move(food);
        for (Node node : nodes) {
            node.draw(g);
        }
    }

    public void changeDirection(Direction direction) {
        this.getHead().direction = direction;
    }

    private Node getHead() {
        return this.nodes.getFirst();
    }

    public void move(Food food) {
        // 获取原来的蛇头
        Node oldHead = this.getHead();
        // 根据方向创建新的蛇头
        Node newHead = Node.builder(this.calculateNewHeadPoint(oldHead.point, oldHead.direction))
                .head(oldHead.direction)
                .build();
        // 将新的蛇头添加到头部
        this.nodes.addFirst(newHead);
        // 将原来的蛇头变成蛇身
        oldHead.isHead = false;
        if (ateFood(food)) {
            // 生成新的食物
            food.random(this);
        } else {
            // 移除蛇尾实现移动
            this.nodes.pollLast();
        }
    }

    public boolean isSnakeBody(Point point) {
        for (Node node : nodes) {
            if (node.point.equals(point)) {
                return true;
            }
        }
        return false;
    }

    private boolean ateFood(Food food) {
        return this.getHead().point.equals(food.getPoint());
    }

    private Point calculateNewHeadPoint(Point point, Direction direction) {
        int x = point.x;
        int y = point.y;
        switch (direction) {
            case UP:
                y--;
                if (y < 0) {
                    y = Constants.MAX_Y;
                }
                break;
            case DOWN:
                y++;
                if (y >= Constants.MAX_Y) {
                    y = 0;
                }
                break;
            case LEFT:
                x--;
                if (x < 0) {
                    x = Constants.MAX_X;
                }
                break;
            case RIGHT:
                x++;
                if (x >= Constants.MAX_X) {
                    x = 0;
                }
                break;
            default:
                break;
        }
        return new Point(x, y);
    }

    private static class Node {
        private final Point point;
        private boolean isHead;
        private Direction direction;

        public Node(Point point, Direction direction, boolean isHead) {
            this.point = point;
            this.direction = direction;
            this.isHead = isHead;
        }

        public static NodeBuilder builder(Point point) {
            return new NodeBuilder(point);
        }

        public void draw(Graphics g) {
            Image image = null;
            if (isHead) {
                switch (direction) {
                    case UP:
                        image = Img.UP;
                        break;
                    case DOWN:
                        image = Img.DOWN;
                        break;
                    case LEFT:
                        image = Img.LEFT;
                        break;
                    case RIGHT:
                        image = Img.RIGHT;
                        break;
                    default:
                        break;
                }

            } else {
                image = Img.BODY;
            }
            if (image != null) {
                g.drawImage(image, point.x * SNAKE_NODE_WIDTH, point.y * SNAKE_NODE_HEIGHT, null);
            }
        }

        public Point getPoint() {
            return point;
        }

        private static class NodeBuilder {
            private final Point point;
            private Direction direction;
            private boolean isHead;

            public NodeBuilder(Point point) {
                this.point = point;
            }

            public NodeBuilder head(Direction direction) {
                this.direction = direction;
                this.isHead = true;
                return this;
            }

            public NodeBuilder body() {
                this.isHead = false;
                this.direction = null;
                return this;
            }

            public Node build() {
                return new Node(this.point, this.direction, this.isHead);
            }
        }
    }
}
