package fun.xiaorang.leetcode.editor.cn;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/minimum-size-subarray-sum/" style="font-weight:bold;font-size:11px;">LeetCode.209.长度最小的子数组<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:56:43
 */
public class MinimumSizeSubarraySum_209 {
    public static void main(String[] args) {
        Solution solution = new MinimumSizeSubarraySum_209().new Solution();
        int[] nums = {2, 3, 1, 2, 4, 3};
        int target = 7;
        int length = solution.minSubArrayLen(target, nums);
        System.out.println("length = " + length);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int minSubArrayLen(int target, int[] nums) {
            int sum = 0, i = 0, result = Integer.MAX_VALUE, subLength;
            for (int j = 0; j < nums.length; j++) {
                sum += nums[j];
                while (sum >= target) {
                    subLength = j - i + 1;
                    result = Integer.min(result, subLength);
                    sum -= nums[i++];
                }
            }
            return result == Integer.MAX_VALUE ? 0 : result;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}