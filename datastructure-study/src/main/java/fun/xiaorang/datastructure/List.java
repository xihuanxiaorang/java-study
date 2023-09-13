package fun.xiaorang.datastructure;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/8/24 13:38
 */
public interface List<E> {
    /**
     * 返回集合中的元素数量
     *
     * @return 元素数量
     */
    int size();

    /**
     * 判断集合是否为空
     *
     * @return {@code true} 如果集合中没有元素的话
     */
    boolean isEmpty();

    /**
     * 判断集合中是否存在指定元素
     *
     * @param o 待检查的元素
     * @return {@code true} 如果集合中存在指定元素的话
     */
    boolean contains(Object o);

    /**
     * 返回指定元素在集合中首次出现的索引
     *
     * @param o 待检查的元素
     * @return 索引，如果集合中不存在指定元素的话则返回 {@code -1}
     */
    int indexOf(Object o);

    /**
     * 返回指定元素在集合中最后一次出现的索引
     *
     * @param o 待检查的元素
     * @return 索引，如果集合中不存在指定元素的话则返回 {@code -1}
     */
    int lastIndexOf(Object o);

    /**
     * 返回集合中指定位置的元素
     *
     * @param index - 要返回元素的索引
     * @return 集合中指定位置的元素
     * @throws IndexOutOfBoundsException - 如果索引超过范围（索引 < 0 || 索引 >= size()）
     */
    E get(int index);

    /**
     * 将集合中指定位置的元素替换为指定元素并返回先前位于指定位置的元素
     *
     * @param index 要替换元素的索引
     * @param e     要存储在指定位置的元素
     * @return 先前位于指定位置的元素
     * @throws IndexOutOfBoundsException – 如果索引超过范围（索引 < 0 || 索引 >= size()）
     */
    E set(int index, E e);

    /**
     * 删除集合中指定位置的元素并返回先前位于指定位置的元素
     *
     * @param index 要删除的元素的索引
     * @return 先前位于指定位置的元素
     * @throws IndexOutOfBoundsException – 如果索引超过范围（索引 < 0 || 索引 >= size()）
     */
    E remove(int index);

    /**
     * 从集合中删除指定元素的第一个匹配项
     *
     * @param o 要从集合中删除的元素
     * @return {@code true} 如果集合中存在指定要删除的元素的话
     */
    boolean remove(Object o);

    /**
     * 添加元素
     *
     * @param e 待添加的元素
     * @return {@code true} 如果元素添加成功
     */
    boolean add(E e);

    /**
     * 在指定位置添加元素
     *
     * @param index 索引
     * @param e     待添加的元素
     * @return {@code true} 如果元素添加成功的话
     */
    boolean add(int index, E e);

    /**
     * 清空集合中的所有元素
     */
    void clear();
}
