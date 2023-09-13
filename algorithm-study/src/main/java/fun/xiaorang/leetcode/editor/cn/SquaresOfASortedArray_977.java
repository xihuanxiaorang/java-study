package fun.xiaorang.leetcode.editor.cn;

import java.util.Arrays;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/squares-of-a-sorted-array/" style="font-weight:bold;font-size:11px;">LeetCode.977.有序数组的平方<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:55:54
 */
public class SquaresOfASortedArray_977 {
    public static void main(String[] args) {
        Solution solution = new SquaresOfASortedArray_977().new Solution();
        int[] nums = {-7, -3, 2, 3, 11};
        int[] res = solution.sortedSquares(nums);
        System.out.println(Arrays.toString(res));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] sortedSquares(int[] nums) {
            int[] res = new int[nums.length];
            int k = nums.length - 1;
            for (int i = 0, j = nums.length - 1; i <= j; ) {
                if (nums[i] * nums[i] > nums[j] * nums[j]) {
                    res[k--] = nums[i] * nums[i];
                    i++;
                } else {
                    res[k--] = nums[j] * nums[j];
                    j--;
                }
            }
            return res;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}