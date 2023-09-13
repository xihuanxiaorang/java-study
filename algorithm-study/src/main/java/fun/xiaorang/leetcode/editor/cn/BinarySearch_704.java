package fun.xiaorang.leetcode.editor.cn;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/binary-search/" style="font-weight:bold;font-size:11px;">LeetCode.704.二分查找<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:52:28
 */
public class BinarySearch_704 {
    public static void main(String[] args) {
        int[] nums = {-1, 0, 3, 5, 9, 12};
        int t = 9;
        Solution solution = new BinarySearch_704().new Solution();
        int index = solution.search(nums, t);
        System.out.println("index: " + index);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 二分搜索（左闭右闭区间）
         *
         * @param nums   数组
         * @param target 目标元素
         * @return 目标元素所在数组中的下标，如果不存在的话，则返回 -1
         */
        public int search(int[] nums, int target) {
            int left = 0, right = nums.length - 1;
            while (left <= right) {
                // int mid = (left + right) >> 1;
                // 避免做加法时导致整型数据溢出
                int mid = left + ((right - left) >> 1);
                if (nums[mid] == target) {
                    return mid;
                } else if (nums[mid] > target) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            return -1;
        }

        /**
         * 二分搜索（左闭右开区间）
         *
         * @param nums   数组
         * @param target 目标元素
         * @return 目标元素所在数组中的下标，如果不存在的话，则返回 -1
         */
        public int search2(int[] nums, int target) {
            int left = 0, right = nums.length;
            while (left < right) {
                int mid = left + ((right - left) >> 1);
                if (nums[mid] == target) {
                    return mid;
                } else if (nums[mid] > target) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            return -1;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}