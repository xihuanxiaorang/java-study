package fun.xiaorang.leetcode.editor.cn;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/remove-linked-list-elements/" style="font-weight:bold;font-size:11px;">LeetCode.203.移除链表元素<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:58:20
 */
public class RemoveLinkedListElements_203 {
    public static void main(String[] args) {
        Solution solution = new RemoveLinkedListElements_203().new Solution();
        ListNode head = buildLinkedList(1, 2, 6, 3, 4, 5, 6);
        ListNode listNode = solution.removeElements(head, 6);
        System.out.println(listNode);
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

    static class ListNode {
        int val;
        ListNode next;

        public ListNode() {
        }

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 从链表中移除所有指定值的节点。
         *
         * @param head 链表的头节点。
         * @param val  要从链表中移除的值。
         * @return 移除节点后的链表的头节点。
         */
        public ListNode removeElements(ListNode head, int val) {
            // 跳过链表开头所有值为 val 的节点
            while (head != null && head.val == val) {
                head = head.next;
            }
            ListNode curr = head;
            // 遍历链表以移除值为 val 的节点
            while (curr != null && curr.next != null) {
                if (curr.next.val == val) {
                    curr.next = curr.next.next; // 跳过值为 val 的节点
                } else {
                    curr = curr.next; // 移动到下一个节点
                }
            }
            return head; // 返回移除节点后的链表的头节点
        }

        /**
         * 从链表中移除所有指定值的节点。
         *
         * @param head 链表的头节点。
         * @param val  要从链表中移除的值。
         * @return 移除节点后的链表的头节点。
         */
        public ListNode removeElements2(ListNode head, int val) {
            ListNode dummyHead = new ListNode(-1, head); // 创建一个虚拟头节点，其 next 指向原头节点
            ListNode curr = dummyHead; // 创建指向虚拟头节点的指针
            while (curr.next != null) {
                if (curr.next.val == val) {
                    curr.next = curr.next.next; // 跳过值为 val 的节点
                } else {
                    curr = curr.next; // 移动到下一个节点
                }
            }
            return dummyHead.next; // 返回移除节点后的链表的头节点
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}