package fun.xiaorang.study.algorithm.dividetoconquer;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; ">汉诺塔<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/02/04 18:46
 */
public class TowerOfHanoi {
  public static void main(String[] args) {
    // 调用汉诺塔算法，将2个盘子从A柱移动到C柱，使用B柱作为缓冲柱
    dfs(3, 'A', 'B', 'C');
  }

  /**
   * 解决汉诺塔问题的递归方法
   *
   * @param num 盘子的数量
   * @param src 源柱子
   * @param buf 缓冲柱
   * @param tar 目标柱
   */
  private static void dfs(int num, char src, char buf, char tar) {
    // 基本情况：只有一个盘子时，直接从源柱子移动到目标柱子
    if (num == 1) {
      System.out.println("将第 " + num + " 个盘子从 " + src + " 移动到 " + tar);
      return;
    }
    // 递归步骤：
    // 1. 将前 num-1 个盘子从源柱子移动到缓冲柱
    dfs(num - 1, src, tar, buf);
    // 2. 将第 num 个盘子从源柱子移动到目标柱
    System.out.println("将第 " + num + " 个盘子从 " + src + " 移动到 " + tar);
    // 3. 将前 num-1 个盘子从缓冲柱移动到目标柱，
    dfs(num - 1, buf, src, tar);
  }
}
