package fun.xiaorang.leetcode.editor.cn;

import java.util.Arrays;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/rotate-array/" style="font-weight:bold;font-size:11px;">LeetCode.189.轮转数组<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:53:29
 */
public class RotateArray_189 {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7};
        int k = 8;
        Solution solution = new RotateArray_189().new Solution();
        solution.rotate(nums, k);
        System.out.println(Arrays.toString(nums));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public void rotate(int[] nums, int k) {
            // 取余操作，防止数组越界
            k %= nums.length;
            // 一次轮转等价于三次反转
            reverse(nums, 0, nums.length - 1);
            reverse(nums, 0, k - 1);
            reverse(nums, k, nums.length - 1);
        }

        /**
         * 反转数组中指定区间的元素（双指针）
         *
         * @param nums  数组
         * @param start 起始位置
         * @param end   结束位置
         */
        private void reverse(int[] nums, int start, int end) {
            while (start < end) {
                int temp = nums[start];
                nums[start] = nums[end];
                nums[end] = temp;
                start++;
                end--;
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}