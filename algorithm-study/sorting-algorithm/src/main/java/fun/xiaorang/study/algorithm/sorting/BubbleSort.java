package fun.xiaorang.study.algorithm.sorting;

import java.util.Arrays;

import static cn.hutool.core.util.PrimitiveArrayUtil.swap;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; ">冒泡排序<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/17 18:36
 */
public class BubbleSort {
  public static void main(String[] args) {
    int[] nums = new int[]{3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
    System.out.println("排序前：" + Arrays.toString(nums));
    bubbleSort(nums);
    System.out.println("排序后：" + Arrays.toString(nums));
  }

  /**
   * 冒泡排序
   *
   * @param arr 待排序数组
   */
  private static void bubbleSort(int[] arr) {
    // 用于标记最后一次交换的位置
    int lastExchangeIndex = 0;
    // 用于标记无序数列的边界，每轮只需要比较到这里即可退出
    int sortBorder = arr.length - 1;
    // 外循环：控制比较的轮数
    for (int i = arr.length - 1; i > 0; i--) {
      // 用于标记本轮是否发生元素交换
      boolean swapped = false;
      // 内循环：将未排序区间 [0, sortBorder] 中的最大元素交换至该区间的最右端
      for (int j = 0; j < sortBorder; j++) {
        // 如果前一个元素大于后一个元素，则交换位置
        if (arr[j] > arr[j + 1]) {
          swap(arr, j, j + 1);
          // 标记发生了交换
          swapped = true;
          // 更新最后一次交换的位置
          lastExchangeIndex = j;
        }
      }
      // 如果本轮没有发生交换，则说明数组已经有序，直接退出循环
      if (!swapped) {
        System.out.println("第 " + (arr.length - i) + " 轮没有发生元素交换，排序提前结束！");
        break;
      }
      // 更新无序数列的边界
      sortBorder = lastExchangeIndex;
      System.out.println("第" + (arr.length - i) + "轮排序后：" + Arrays.toString(arr));
    }
  }
}
