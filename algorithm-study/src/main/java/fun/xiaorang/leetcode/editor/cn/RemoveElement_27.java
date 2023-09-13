package fun.xiaorang.leetcode.editor.cn;

import java.util.Arrays;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/remove-element/" style="font-weight:bold;font-size:11px;">LeetCode.27.移除元素<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:55:10
 */
public class RemoveElement_27 {
    public static void main(String[] args) {
        Solution solution = new RemoveElement_27().new Solution();
        int[] nums = {0, 1, 2, 2, 3, 0, 4, 2};
        int length = solution.removeElement(nums, 2);
        System.out.println("nums: " + Arrays.toString(nums) + "\t" + "length: " + length);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int removeElement(int[] nums, int val) {
            int slow = 0;
            for (int fast = 0; fast < nums.length; fast++) {
                if (nums[fast] != val) {
                    nums[slow++] = nums[fast];
                }
            }
            return slow;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}