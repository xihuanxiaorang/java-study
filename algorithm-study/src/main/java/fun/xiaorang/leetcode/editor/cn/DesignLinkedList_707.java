package fun.xiaorang.leetcode.editor.cn;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/design-linked-list/" style="font-weight:bold;font-size:11px;">LeetCode.707.设计链表<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:59:07
 */
public class DesignLinkedList_707 {
    public static void main(String[] args) {
        MyLinkedList2 myLinkedList = new MyLinkedList2();
        myLinkedList.addAtHead(38);
        myLinkedList.addAtTail(66);
        myLinkedList.addAtTail(61);
        myLinkedList.addAtTail(76);
        myLinkedList.addAtTail(26);
        myLinkedList.addAtTail(37);
        myLinkedList.addAtTail(8);
        myLinkedList.deleteAtIndex(5);
        myLinkedList.addAtHead(4);
        myLinkedList.addAtHead(45);
        System.out.println(myLinkedList);
        System.out.println(myLinkedList.get(4));
    }
}
//leetcode submit region begin(Prohibit modification and deletion)

/**
 * 双向链表
 */
class MyLinkedList {
    /**
     * 虚拟头节点，不存储实际数据
     */
    private final ListNode dummyHead;
    /**
     * 虚拟尾节点，不存储实际数据
     */
    private final ListNode dummyTail;
    /**
     * 链表的当前大小
     */
    private int size;

    /**
     * 构造函数，初始化链表，创建虚拟头节点和虚拟尾节点，同时连接它们
     */
    public MyLinkedList() {
        dummyHead = new ListNode(-1);
        dummyTail = new ListNode(-1);
        dummyHead.next = dummyTail;
        dummyTail.prev = dummyHead;
    }

    /**
     * 获取指定索引位置的节点值
     *
     * @param index 要获取的节点的索引。索引从 0 开始
     * @return 如果索引有效，则返回对应节点的值；否则返回-1
     */
    public int get(int index) {
        if (index < 0 || index >= size) {
            return -1;
        }
        ListNode curr;
        if (index < (size >> 1)) {
            curr = dummyHead.next;
            while (index > 0) {
                curr = curr.next;
                index--;
            }
        } else {
            curr = dummyTail;
            while (index < size) {
                curr = curr.prev;
                index++;
            }
        }
        return curr.val;
    }

    /**
     * 在链表头部添加一个新节点
     *
     * @param val 要插入的节点的值
     */
    public void addAtHead(int val) {
        this.addAtIndex(0, val);
    }

    /**
     * 在链表尾部添加一个新节点
     *
     * @param val 要插入的节点的值
     */
    public void addAtTail(int val) {
        this.addAtIndex(size, val);
    }

    /**
     * 在指定索引位置插入一个新节点
     *
     * @param index 要插入的节点的索引。索引从 0 开始
     * @param val   要插入的节点的值
     */
    public void addAtIndex(int index, int val) {
        // 检查索引是否有效，如果索引无效则不执行插入操作
        if (index < 0 || index > size) {
            return;
        }
        // 获取指定索引位置的前一个节点
        ListNode prev = getPrevNode(index);
        // 创建新节点并插入到链表中
        ListNode newNode = new ListNode(val, prev, prev.next);
        prev.next.prev = newNode;
        prev.next = newNode;
        // 更新链表大小
        size++;
    }

    /**
     * 删除指定索引位置的节点
     *
     * @param index 要删除的节点的索引。索引从 0 开始
     */
    public void deleteAtIndex(int index) {
        // 检查索引是否有效，如果索引无效则不执行删除操作
        if (index < 0 || index >= size) {
            return;
        }
        // 获取指定索引位置的前一个节点
        ListNode prev = getPrevNode(index);
        // 删除节点：将前一个节点的 next 指向待删除节点的下一个节点
        prev.next.next.prev = prev;
        prev.next = prev.next.next;
        // 更新链表大小
        size--;
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
        ListNode curr = dummyHead.next;
        while (curr != null && curr.next != null) {
            sb.append(curr.val);
            if (curr.next.next != null) {
                sb.append(", ");
            }
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 获取指定索引位置的前一个节点
     *
     * @param index 要获取前一个节点的索引。索引从 0 开始
     * @return 前一个节点的引用
     */
    private ListNode getPrevNode(int index) {
        ListNode prev;
        // 判断索引位置是在链表的前半部分还是后半部分
        if (index < (size >> 1)) {
            prev = dummyHead;
            // 在前半部分从头向后遍历，找到指定索引位置的前一个节点
            while (index > 0) {
                prev = prev.next;
                index--;
            }
        } else {
            prev = dummyTail;
            // 在后半部分从尾向前遍历，找到指定索引位置的前一个节点
            while (index <= size) {
                prev = prev.prev;
                index++;
            }
        }
        return prev;
    }

    /**
     * 内部节点类，表示链表节点
     */
    private static class ListNode {
        /**
         * 节点的值
         */
        private final int val;
        /**
         * 指向前一个节点的引用
         */
        private ListNode prev;
        /**
         * 指向下一个节点的引用
         */
        private ListNode next;

        /**
         * 构造函数，初始化节点
         *
         * @param val 节点的值
         */
        public ListNode(int val) {
            this.val = val;
        }

        /**
         * 构造函数，初始化节点，并设置前一个节点和后一个节点的引用
         *
         * @param val  节点的值
         * @param prev 前一个节点的引用
         * @param next 后一个节点的引用
         */
        public ListNode(int val, ListNode prev, ListNode next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)

/**
 * 单向链表
 */
class MyLinkedList2 {
    /**
     * 虚拟头节点，不存储实际数据
     */
    private final ListNode dummyHead;
    /**
     * 链表的当前大小
     */
    private int size;

    /**
     * 构造函数，初始化链表，创建虚拟头节点
     */
    public MyLinkedList2() {
        dummyHead = new ListNode(-1);
    }

    /**
     * 获取指定索引位置的节点值
     *
     * @param index 索引位置，从0开始
     * @return 如果索引有效，则返回对应节点的值；否则返回-1
     */
    public int get(int index) {
        if (index < 0 || index >= size) {
            return -1;
        }
        ListNode curr = dummyHead.next;
        while (index > 0) {
            curr = curr.next;
            index--;
        }
        return curr.val;
    }

    /**
     * 在链表头部添加一个新节点
     *
     * @param val 要插入的节点的值
     */
    public void addAtHead(int val) {
        this.addAtIndex(0, val);
    }

    /**
     * 在链表尾部添加一个新节点
     *
     * @param val 要插入的节点的值
     */
    public void addAtTail(int val) {
        this.addAtIndex(size, val);
    }

    /**
     * 在指定索引位置插入一个新节点
     *
     * @param index 索引位置，从0开始
     * @param val   要插入的节点的值
     */
    public void addAtIndex(int index, int val) {
        // 检查索引是否有效，如果索引无效则不执行插入操作
        if (index < 0 || index > size) {
            return;
        }
        // 找到待插入位置的前一个节点
        ListNode prev = dummyHead;
        while (index > 0) {
            prev = prev.next;
            index--;
        }
        // 创建新节点并将其插入到链表中
        ListNode newNode = new ListNode(val);
        newNode.next = prev.next;
        prev.next = newNode;
        // 更新链表大小
        size++;
    }

    /**
     * 删除指定索引位置的节点。
     *
     * @param index 索引位置，从0开始
     */
    public void deleteAtIndex(int index) {
        // 检查索引是否有效，如果索引无效则不执行删除操作
        if (index < 0 || index >= size) {
            return;
        }
        // 找到待删除节点的前一个节点
        ListNode prev = dummyHead;
        while (index > 0) {
            prev = prev.next;
            index--;
        }
        // 删除节点：将前一个节点的 next 指向待删除节点的下一个节点
        prev.next = prev.next.next;
        // 更新链表大小
        size--;
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
        ListNode curr = dummyHead.next;
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

    /**
     * 内部节点类，表示链表节点
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
         * 构造函数，初始化节点
         *
         * @param val 节点的值
         */
        public ListNode(int val) {
            this.val = val;
        }
    }
}