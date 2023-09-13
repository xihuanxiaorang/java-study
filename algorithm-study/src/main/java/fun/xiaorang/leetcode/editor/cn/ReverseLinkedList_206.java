package fun.xiaorang.leetcode.editor.cn;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/reverse-linked-list/" style="font-weight:bold;font-size:11px;">LeetCode.206.反转链表<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:59:52
 */
public class ReverseLinkedList_206 {
    public static void main(String[] args) {
        Solution solution = new ReverseLinkedList_206().new Solution();
        ListNode head = buildLinkedList(1, 2, 3, 4, 5);
        System.out.println("链表反转前: " + head);
        ListNode listNode = solution.reverseList(head);
        System.out.println("链表反转后: " + listNode);
    }

    /**
     * 使用尾插法构建链表
     *
     * @param nums 要构建链表的节点值数组
     * @return 构建好的链表的头节点
     */
    private static ListNode buildLinkedList(int... nums) {
        ListNode dummyHead = new ListNode(-1); // 创建一个虚拟头节点
        ListNode tail = dummyHead; // 初始化尾部指针为虚拟头节点
        for (int num : nums) {
            ListNode newNode = new ListNode(num); // 创建新节点
            tail.next = newNode; // 将尾部指针的下一个节点指向新节点
            tail = newNode; // 更新尾部指针为新节点
        }
        return dummyHead.next; // 返回真正的链表头节点
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            ListNode curr = this;
            while (curr != null) {
                sb.append(curr.val);
                if (curr.next != null) {
                    sb.append(", ");
                }
                curr = curr.next;
            }
            sb.append("]");
            return sb.toString();
        }
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 反转链表（双指针解法）
         *
         * @param head 链表的头节点
         * @return 反转后的链表的头节点
         */
        public ListNode reverseList(ListNode head) {
            // 初始化前驱节点为null，当前节点为链表头
            ListNode prev = null, curr = head;
            // 遍历链表
            while (curr != null) {
                // 保存当前节点的下一个节点
                ListNode next = curr.next;
                // 将当前节点的next指向前驱节点，实现反转
                curr.next = prev;
                // 更新前驱节点为当前节点，当前节点为下一个节点
                prev = curr;
                curr = next;
            }
            // 返回反转后的链表的头节点
            return prev;
        }

        /**
         * 反转链表（递归解法）
         *
         * @param head 链表的头节点
         * @return 反转后的链表的头节点
         */
        public ListNode reverseList2(ListNode head) {
            // 调用递归方法，初始时前驱节点为null，当前节点为链表头节点
            return reverse(null, head);
        }

        /**
         * 递归反转链表的辅助方法
         *
         * @param prev 前驱节点
         * @param curr 当前节点
         * @return 反转后的链表的头节点
         */
        private ListNode reverse(ListNode prev, ListNode curr) {
            // 递归终止条件：当前节点为null，返回前驱节点作为新的头节点
            if (curr == null) {
                return prev;
            }
            // 保存当前节点的下一个节点
            ListNode next = curr.next;
            // 将当前节点的next指向前驱节点，实现反转
            curr.next = prev;
            // 递归调用，将当前节点作为前驱节点，下一个节点作为当前节点
            return reverse(curr, next);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}