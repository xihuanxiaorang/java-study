package fun.xiaorang.leetcode.editor.cn;

import java.util.Arrays;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/sort-an-array/" style="font-weight:bold;font-size:11px;">LeetCode.912.排序数组<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-15 16:26:00
 */
public class SortAnArray_912 {
    public static void main(String[] args) {
        Solution solution = new SortAnArray_912().new Solution();
        int[] nums = new int[]{5, 1, 1, 2, 0, 0};
        solution.sortArray(nums);
        System.out.println(Arrays.toString(nums));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] sortArray(int[] nums) {
//            bubbleSort(nums);
            selectionSort(nums);
            return nums;
        }

        /**
         * 选择排序
         *
         * @param nums 待排序的数组
         */
        private void selectionSort(int[] nums) {
            // 外层循环控制比较的轮数，最多需要比较 n-1 轮
            // 每一轮比较之后，无序区的长度都会减1
            // 所以下一轮比较的时候，内层循环的边界就是上一轮比较的最后位置
            // 由于最后一轮只剩一个元素，所以不需要比较，因此 i < nums.length - 1
            for (int i = 0; i < nums.length - 1; i++) {
                // 用于记录最小元素的下标
                int minIndex = i;
                // 内层循环用于比较无序区的元素并找出最小元素的下标
                for (int j = i + 1; j < nums.length; j++) {
                    // 如果当前元素比最小元素小，更新最小元素的下标
                    if (nums[j] < nums[minIndex]) {
                        // 更新最小元素的下标
                        minIndex = j;
                    }
                }
                // 如果最小元素不是当前元素，则交换位置
                if (minIndex != i) {
                    swap(nums, i, minIndex);
                }
            }
        }

        /**
         * 冒泡排序
         *
         * @param nums 待排序的数组
         */
        private void bubbleSort(int[] nums) {
            // 记录最后一次交换的位置
            int lastExchangeIndex = nums.length - 1;
            // 外层循环控制比较的轮数，最多需要比较 n-1 轮
            for (int i = 0; i < nums.length - 1; i++) {
                // 用于标记是否发生了交换
                boolean swapped = false;
                // 上一次的最后交换位置作为这次循环的边界
                int end = lastExchangeIndex;
                // 内层循环用于比较相邻元素并交换它们的位置
                // 每一轮比较之后，无序区的长度都会减1
                // 所以下一轮比较的时候，内层循环的边界就是上一轮比较的最后位置
                for (int j = 0; j < end; j++) {
                    // 如果当前元素比下一个元素大，交换它们的位置
                    if (nums[j] > nums[j + 1]) {
                        swap(nums, j, j + 1);
                        // 标记发生了交换
                        swapped = true;
                        // 更新最后一次交换的位置
                        lastExchangeIndex = j;
                    }
                }
                if (!swapped) {
                    // 如果没有发生交换，说明数组已经有序，直接退出循环
                    break;
                }
            }
        }

        /**
         * 交换数组中两个元素的位置
         *
         * @param nums 数组
         * @param i    元素1的下标
         * @param j    元素2的下标
         */
        private void swap(int[] nums, int i, int j) {
            int temp = nums[j];
            nums[j] = nums[i];
            nums[i] = temp;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}