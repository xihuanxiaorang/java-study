package fun.xiaorang.game.snake;

import java.awt.*;
import java.util.LinkedList;

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
    /**
     * 蛇的方向
     */
    private final Direction direction = Direction.RIGHT;

    public Snake() {
        this.nodes.add(Node.builder(new Point(3, 2)).head(direction).build());
        this.nodes.add(Node.builder(new Point(2, 2)).body().build());
        this.nodes.add(Node.builder(new Point(1, 2)).body().build());
    }

    public void draw(Graphics g) {
        for (Node node : nodes) {
            node.draw(g);
        }
    }

    private static class Node {
        private final Point point;
        private final Direction direction;
        private final boolean isHead;

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
                g.drawImage(image, point.x * Constants.SNAKE_NODE_WIDTH, point.y * Constants.SNAKE_NODE_HEIGHT, null);
            }
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
                return this;
            }

            public Node build() {
                return new Node(this.point, this.direction, this.isHead);
            }
        }
    }
}
