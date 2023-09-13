package fun.xiaorang.leetcode.editor.cn;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/merge-sorted-array/" style="font-weight:bold;font-size:11px;">LeetCode.88.合并两个有序数组<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:50:51
 */
public class MergeSortedArray_88 {
    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 0, 0, 0};
        int[] nums2 = {2, 5, 6};
        int m = 3, n = 3;
        Solution solution = new MergeSortedArray_88().new Solution();
        solution.merge(nums1, m, nums2, n);
        for (int i = 0; i < nums1.length; i++) {
            System.out.println(nums1[i]);
        }
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public void merge(int[] nums1, int m, int[] nums2, int n) {
            int i = m - 1, j = n - 1, k = m + n - 1;
            while (i >= 0 && j >= 0) {
                if (nums1[i] < nums2[j]) {
                    nums1[k] = nums2[j--];
                } else {
                    nums1[k] = nums1[i--];
                }
                k--;
            }
            // 因为本身就是往 nums1 数组中放入元素，所以只需判断 nums2 数组是否遍历完即可！
            while (j >= 0) {
                nums1[k--] = nums2[j--];
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}