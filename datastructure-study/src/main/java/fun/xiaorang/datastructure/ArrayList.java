package fun.xiaorang.datastructure;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">顺序表 | 动态数组<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/8/24 13:37
 */
public class ArrayList<E> implements List<E> {
    /**
     * 默认的初始容量
     */
    private static final int DEFAULT_CAPACITY = 10;
    
    /**
     * 用于空实例的共享空数组实例
     */
    private static final Object[] EMPTY_ELEMENT_DATA = {};

    /**
     * 用于默认大小(10)的空实例的共享空数组实例。
     * 将它与 EMPTY_ELEMENT_DATA 区别开来，以了解添加第一个元素时要膨胀多少
     */
    private static final Object[] DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA = {};

    /**
     * 数组缓冲区，数组列表的元素被存储在其中。数组列表的容量就是这个数组缓冲区的长度。
     * 当添加第一个元素时，如果 data == DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA，则进行第一次扩容，容量大小变为 DEFAULT_CAPACITY。
     */
    private Object[] elementData;

    /**
     * 数组中元素的数量
     */
    private int size;

    /**
     * 无参构造函数
     */
    public ArrayList() {
        elementData = new Object[DEFAULT_CAPACITY];
    }

    /**
     * 构造具有指定初始容量的空数组
     *
     * @param initialCapacity 初始容量大小
     * @throws IllegalArgumentException 如果指定的初始容量为负数
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        } else if (initialCapacity > 0) {
            elementData = new Object[initialCapacity];
        } else {
            elementData = EMPTY_ELEMENT_DATA;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOfRange(o, 0, size);
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, size);
        return elementData(index);
    }

    @Override
    public E set(int index, E e) {
        Objects.checkIndex(index, size);
        E oldValue = elementData(index);
        elementData[index] = e;
        return oldValue;
    }

    @Override
    public E remove(int index) {
        Objects.checkIndex(index, size);
        final Object[] es = elementData;
        @SuppressWarnings("unchecked") E oldValue = (E) es[index];
        fastRemove(es, index);
        return oldValue;
    }

    @Override
    public boolean remove(Object o) {
        final Object[] es = elementData;
        final int size = this.size;
        int index = 0;
        found:
        {
            if (o == null) {
                for (; index < size; index++) {
                    if (es[index] == null) {
                        break found;
                    }
                }
            } else {
                for (; index < size; index++) {
                    if (o.equals(es[index])) {
                        break found;
                    }
                }
            }
            return false;
        }
        fastRemove(es, index);
        return true;
    }

    private void fastRemove(Object[] es, int index) {
        final int newSize;
        if ((newSize = size - 1) > index) {
            System.arraycopy(es, index + 1, es, index, newSize - index);
        }
        es[size = newSize] = null;
    }

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    private int lastIndexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = end - 1; i >= start; i--) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = end - 1; i >= start; i--) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int indexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean add(E e) {
        if (elementData.length == size) {
            grow();
        }
        elementData[size++] = e;
        return true;
    }

    @Override
    public boolean add(int index, E e) {
        rangeCheckForAdd(index);
        if (elementData.length == size) {
            elementData = grow();
        }
        // 效果：将 index 索引后的所有元素全部往后挪动一格
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = e;
        size++;
        return true;
    }

    @Override
    public void clear() {
        final Object[] es = elementData;
        for (int i = 0; i < size; i++) {
            es[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            E e = elementData(i);
            sb.append(e);
            if (i < size - 1) {
                sb.append(",").append(' ');
            }
        }
        return sb.append("]").toString();
    }

    /**
     * 检查添加元素时索引是否越界，如果索引越界的话，则抛出 {@code IndexOutOfBoundsException} 异常
     *
     * @param index 索引
     * @throws IndexOutOfBoundsException 索引越界异常
     */
    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + size);
        }
    }

    /**
     * 扩容
     *
     * @return 扩容后的新数组
     */
    private Object[] grow() {
        return grow(size + 1);
    }

    /**
     * 扩容
     *
     * @param minCapacity 最小容量
     * @return 扩容后的新数组
     */
    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0 || elementData != DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA) {
            int newCapacity = oldCapacity + Math.max(minCapacity - size, minCapacity >> 1);
            System.out.printf("grow: oldCapacity = %d => newCapacity = %d%n", oldCapacity, newCapacity);
            return Arrays.copyOf(elementData, newCapacity);
        } else {
            int newCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
            System.out.printf("grow: oldCapacity = %d => newCapacity = %d%n", oldCapacity, newCapacity);
            return new Object[DEFAULT_CAPACITY];
        }
    }
}
