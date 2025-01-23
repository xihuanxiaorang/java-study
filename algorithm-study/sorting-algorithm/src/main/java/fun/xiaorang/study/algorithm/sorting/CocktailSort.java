package fun.xiaorang.study.algorithm.sorting;

import java.util.Arrays;

import static cn.hutool.core.util.PrimitiveArrayUtil.swap;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; ">鸡尾酒排序（双向冒泡）<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/18 11:29
 */
public class CocktailSort {
  public static void main(String[] args) {
//    int[] nums = new int[]{3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
    int[] nums = new int[]{2, 3, 4, 5, 6, 7, 8, 1};
    System.out.println("排序前：" + Arrays.toString(nums));
    cocktailSort(nums);
    System.out.println("排序后：" + Arrays.toString(nums));
  }

  /**
   * 鸡尾酒排序
   *
   * @param arr 待排序数组
   */
  private static void cocktailSort(int[] arr) {
    // 用于标记无序数列的左边界
    int left = 0;
    // 用于标记无序数列的右边界
    int right = arr.length - 1;
    // 用于标记最后一次交换的位置
    int lastExchangeIndex = 0;
    // 用于标记是否发生了元素交换
    boolean swapped;
    do {
      // 重置标记
      swapped = false;
      // 从左到右遍历，将最大的元素放到右边
      for (int j = left; j < right; j++) {
        if (arr[j] > arr[j + 1]) {
          swap(arr, j, j + 1);
          // 标记发生了交换
          swapped = true;
          // 更新最后一次交换的位置
          lastExchangeIndex = j;
        }
      }
      // 如果没有元素交换，则说明数组已经有序，直接退出循环
      if (!swapped) {
        break;
      }
      // 更新无序数列的右边界
      right = lastExchangeIndex;
      // 重置标记
      swapped = false;
      // 从右到左遍历，将最小的元素放到左边
      for (int j = right; j > left; j--) {
        if (arr[j] < arr[j - 1]) {
          swap(arr, j, j - 1);
          // 标记发生了交换
          swapped = true;
          // 更新最后一次交换的位置
          lastExchangeIndex = j;
        }
      }
      // 更新无序数列的左边界
      left = lastExchangeIndex;
    } while (swapped); // 如果没有发生交换，则说明数组已经有序，直接退出循环
  }
}
