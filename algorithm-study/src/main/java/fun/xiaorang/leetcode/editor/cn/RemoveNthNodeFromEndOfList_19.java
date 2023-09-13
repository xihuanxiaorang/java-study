package fun.xiaorang.leetcode.editor.cn;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/remove-nth-node-from-end-of-list/" style="font-weight:bold;font-size:11px;">LeetCode.19.删除链表的倒数第 N 个结点<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 23:01:29
 */
public class RemoveNthNodeFromEndOfList_19 {
    public static void main(String[] args) {
        Solution solution = new RemoveNthNodeFromEndOfList_19().new Solution();
        ListNode head = buildLinkedList(1, 2);
        int n = 1;
        System.out.println("删除链表的倒数第" + n + "个结点前: " + head);
        ListNode newHead = solution.removeNthFromEnd(head, n);
        System.out.println("删除链表的倒数第" + n + "个结点后: " + newHead);
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
        // 遍历节点值数组，创建新节点并连接到链表尾部
        for (int num : nums) {
            ListNode newNode = new ListNode(num); // 创建新节点
            tail.next = newNode; // 将尾部指针的下一个节点指向新节点
            tail = newNode; // 更新尾部指针为新节点
        }
        return dummyHead.next; // 返回真正的链表头节点
    }


    /**
     * 链表节点类。
     */
    private static class ListNode {
        /**
         * 节点的值
         */
        int val;

        /**
         * 指向下一个节点的引用
         */
        ListNode next;

        /**
         * 无参构造函数，创建一个空节点
         */
        ListNode() {
        }

        /**
         * 构造函数，创建一个节点并初始化其值
         *
         * @param val 节点的值
         */
        ListNode(int val) {
            this.val = val;
        }

        /**
         * 构造函数，创建一个节点并初始化其值和下一个节点的引用
         *
         * @param val  节点的值
         * @param next 指向下一个节点的引用
         */
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        /**
         * 将链表转换为字符串以便于打印和显示
         *
         * @return 表示链表的字符串，格式为"[val1,val2,...]"
         */
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
         * 删除链表中倒数第 n 个节点
         *
         * @param head 链表的头节点
         * @param n    要删除的节点的倒数第 n 个位置
         * @return 删除节点后的链表的头节点
         */
        public ListNode removeNthFromEnd(ListNode head, int n) {
            // 创建一个虚拟头节点
            ListNode dummyHead = new ListNode(-1, head);
            // 初始化快慢指针
            ListNode slow = dummyHead, fast = dummyHead;
            // 将 n 增加 1，使得快指针领先慢指针 n 个节点，为了找到链表中倒数第 n 个节点的前一个节点
            n += 1;
            // 快指针先移动 n 个节点
            while (n-- > 0 && fast != null) {
                fast = fast.next;
            }
            // 同时移动快慢指针，直到快指针到达链表尾部
            while (fast != null) {
                slow = slow.next;
                fast = fast.next;
            }
            // 删除倒数第 n 个节点
            slow.next = slow.next.next;
            // 返回删除节点后的链表头节点
            return dummyHead.next;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}