package fun.xiaorang.leetcode.editor.cn;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/swap-nodes-in-pairs/" style="font-weight:bold;font-size:11px;">LeetCode.24.两两交换链表中的节点<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 23:00:43
 */
public class SwapNodesInPairs_24 {
    public static void main(String[] args) {
        Solution solution = new SwapNodesInPairs_24().new Solution();
        ListNode head = buildLinkedList(1, 2, 3, 4, 5);
        System.out.println("链表两两交换前: " + head);
        ListNode newHead = solution.swapPairs(head);
        System.out.println("链表两两交换后: " + newHead);
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

        ListNode() {
        }

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
         * 交换链表中相邻的节点，每两个节点进行一次交换
         *
         * @param head 链表的头节点
         * @return 交换后的链表的头节点
         */
        public ListNode swapPairs(ListNode head) {
            // 创建虚拟头节点，并将其next指向原链表的头节点
            ListNode dummyHead = new ListNode(-1, head);
            // 初始化当前节点为虚拟头节点
            ListNode curr = dummyHead;
            // 遍历链表，每次交换两个相邻节点
            while (curr.next != null && curr.next.next != null) {
                // 保存当前节点的下一个节点
                ListNode temp = curr.next;
                // 保存当前节点的下下下一个节点
                ListNode temp1 = curr.next.next.next;
                // 将当前节点的next指向下下一个节点
                curr.next = curr.next.next;
                // 将下下一个节点的next指向下一个节点
                curr.next.next = temp;
                // 将下一个节点的next指向下下下一个节点，即原链表（不带虚拟头节点）中的第三个节点
                temp.next = temp1;
                // 更新当前节点为下下一个节点
                curr = curr.next.next;
            }
            // 返回交换后的链表的头节点
            return dummyHead.next;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}