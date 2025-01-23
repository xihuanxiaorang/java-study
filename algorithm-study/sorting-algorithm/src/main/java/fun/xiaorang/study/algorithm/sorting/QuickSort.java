package fun.xiaorang.study.algorithm.sorting;

import java.util.Arrays;

import static cn.hutool.core.util.PrimitiveArrayUtil.swap;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; ">快速排序<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/19 23:28
 */
public class QuickSort {
  public static void main(String[] args) {
    int[] nums = new int[]{3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
    System.out.println("排序前：" + Arrays.toString(nums));
    quickSort(nums, 0, nums.length - 1);
    System.out.println("排序后：" + Arrays.toString(nums));
  }

  /**
   * 快速排序，对数组的指定区间进行排序（尾递归优化）
   *
   * @param arr   待排序数组
   * @param left  当前区间的起始索引
   * @param right 当前区间的结束索引
   */
  private static void quickSort(int[] arr, int left, int right) {
    // 递归结束条件：区间内只有一个元素
    while (left < right) {
      // 哨兵划分操作，返回基准元素的索引
      int pivot = partition(arr, left, right);
      // 对两个子数组中较短的那个执行快速排序
      if (pivot - left < right - pivot) {
        // 递归排序左侧子数组
        quickSort(arr, left, pivot - 1);
        // 剩余未排序区间为 [pivot + 1, right]
        left = pivot + 1;
      } else {
        // 递归排序右侧子数组
        quickSort(arr, pivot + 1, right);
        // 剩余未排序区间为 [left, pivot - 1]
        right = pivot - 1;
      }
    }
  }

  /**
   * 哨兵划分（分区）
   * 它重新排列数组中的元素，使得所有小于基准数的元素都在其左侧，而所有大于基准数的元素都在其右侧
   *
   * @param arr   待分区的数组
   * @param left  分区的起始索引
   * @param right 分区的结束索引
   * @return 分区后基准元素的索引
   */
  private static int partition(int[] arr, int left, int right) {
    // 选取三个候选元素的中位数作为基准元素
    int med = medianThree(arr, left, left + (right - left) / 2, right);
    // 将基准元素交换至数组最左端
    swap(arr, left, med);
    int i = left, j = right;
    while (i < j) {
      // 向左移动 j 指针，直至找到一个小于基准数的元素
      while (i < j && arr[j] >= arr[left]) {
        j--;
      }
      // 向右移动 i 指针，直至找到一个大于基准数的元素
      while (i < j && arr[i] <= arr[left]) {
        i++;
      }
      // 交换 i 和 j 位置的元素
      swap(arr, i, j);
    }
    // 将基准元素与 i 位置的元素交换
    swap(arr, i, left);
    // 返回基准元素的索引
    return i;
  }

  /**
   * 选取三个候选元素的中位数作为基准元素（三点取中法），可以有效避免快速排序在近乎有序的数组中退化为 O(n^2) 的时间复杂度
   *
   * @param arr   待排序数组
   * @param left  左边界
   * @param mid   中间值
   * @param right 右边界
   * @return 三个候选元素的中位数
   */
  private static int medianThree(int[] arr, int left, int mid, int right) {
    int l = arr[left], m = arr[mid], r = arr[right];
    if ((l <= m && m <= r) || (r <= m && m <= l)) {
      return mid;
    } else if ((m <= l && l <= r) || (r <= l && l <= r)) {
      return left;
    } else {
      return right;
    }
  }
}
