package fun.xiaorang.leetcode.editor.cn;

import java.util.Arrays;

/**
 * @author xiaorang
 * @description <a href="https://leetcode.cn/problems/spiral-matrix-ii/" style="font-weight:bold;font-size:11px;">LeetCode.59.螺旋矩阵 II<a/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的码场</a>  - show me the code
 * @date 2023-09-13 22:57:32
 */
public class SpiralMatrixIi_59 {
    public static void main(String[] args) {
        Solution solution = new SpiralMatrixIi_59().new Solution();
        int n = 5;
        int[][] matrix = solution.generateMatrix(n);
        for (int i = 0; i < matrix.length; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        // 全程遵循左闭右开的原则
        public int[][] generateMatrix(int n) {
            int[][] res = new int[n][n];
            int loop = 0, count = 1, start = 0, i, j;
            while (loop++ < n / 2) {
                // 模拟上侧从左至右
                for (j = start; j < n - loop; j++) {
                    res[start][j] = count++;
                }
                // 模拟右侧从上至下
                for (i = start; i < n - loop; i++) {
                    res[i][j] = count++;
                }
                // 模拟下侧从右至左
                for (; j > start; j--) {
                    res[i][j] = count++;
                }
                // 模拟左侧从下至上
                for (; i > start; i--) {
                    res[i][j] = count++;
                }
                start++;
            }
            // 假如n是奇数的话，则会多出来一个，需要在最后的起始点位置赋值
            if (n % 2 == 1) {
                res[start][start] = count;
            }
            return res;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)
}