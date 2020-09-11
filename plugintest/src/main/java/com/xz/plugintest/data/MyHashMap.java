package com.xz.plugintest.data;

import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.plugintest.data  AspectTest
 * @Des MyHashMap
 * @DATE 2020/9/4  11:53 星期五
 *
 * hash map 源码   链表大小超过8换成红黑树存储  减小查找时间复杂度
 */
public class MyHashMap<K,V> {

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node[] tables;  // 链表数组

    /**
     * The next size value at which to resize (capacity * load factor).
     *
     * @serial
     */
    // (The javadoc description is true upon serialization.
    // Additionally, if the table array has not been allocated, this
    // field holds the initial array capacity, or zero signifying
    // DEFAULT_INITIAL_CAPACITY.)
    int threshold;

    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    float loadFactor;

    private int size;

    public MyHashMap(int size,int factor) {
        HashMap map;
        this.loadFactor = factor;
        this.threshold = tableSizeFor(size);
    }

    // 单链表
    class Node<K,V> implements Map.Entry<K,V> {
        private int hash;
        private K k;
        private V v;
        Node next;

        public Node(int hash,K k, V v,Node next) {
            this.hash = hash;
            this.k = k;
            this.v = v;
            this.next = next;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }

        @Override
        public V setValue(V value) {
            V oldValue = v;
            v = value;
            return oldValue;
        }
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 计算 数组大小  为 2 的 幂次方
     * Returns a power of two size for the given target capacity.
     */
    private int tableSizeFor(int cap) {
        int n = cap - 1;  // -1 防止 cap 值本就是 2的幂次方
        n |= n >>> 1;  // 高位 1  相邻的数 逐渐变为1
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public V get(K k) {
        Node<K,V> e;
        return (e = getNode(hash(k),k)) == null ? null : e.getValue();
    }

    private Node<K,V> getNode(int hash,K k) {
        Node<K,V>[] tab;
        Node<K,V> first,e;
        int n;
        if ((tab = tables) != null && (n = tab.length) > 0
           && (first = tab[hash & (n - 1)]) != null) {
            if (first.getKey() != null && first.getKey().equals(k)) {
                return first;
            }
            if ((e = first.next) != null) {
                do {
                    if (e.getKey() != null && e.getKey().equals(k)) {
                        return e;
                    }
                }while ((e = e.next) != null);
            }
        }
        return null;
    }

    public boolean containsKey(K k) {
        return getNode(hash(k),k) != null;
    }

    public V put(K k,V v) {
        return putVal(k,v,hash(k),false,true);
    }

    /**
     * Implements Map.put and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value
     * @param evict if false, the table is in creation mode.
     * @return previous value, or null if none
     */
    private V putVal(K key, V value,int hash,boolean onlyIfAbsent, boolean evict) {
        Node<K,V>[] tab;
        Node<K,V> p;
        int n,i;
        if ((tab = tables) == null || (n = tab.length) == 0) {  // 数组初始化
            n = (tab = resize()).length;
        }
        if ((p = tab[(i = (n - 1) & hash)]) == null) {
            tab[i] = newNode(key,value,hash,null);
        } else {
            Node<K,V> e;
            if (p.getKey() != null && p.getKey().equals(key)) { // 原来key就存在
                e = p;
            } else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(key,value,hash,null);
//                        Node<K, V> kvNode = newNode(key, value, hash, p);
//                        tab[i] = kvNode;
                        break;
                    }
                    if (e.getKey() != null && e.getKey().equals(key)) break;
                    p = e;
                }
            }
            if (e != null) {
                V oldValue = e.getValue();
                if (!onlyIfAbsent || oldValue == null) {
                    e.setValue(value);  // 需要替换原来的值
                }
                return oldValue;
            }
        }
        if (++size > threshold) {
            resize();  // 超过大小 需要进行扩容
        }
        return null;
    }

    private Node<K,V>[] resize() {
        Node<K,V>[] oldTab = tables;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap,newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                oldThr = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY) {  // 扩容两倍
                newThr = oldThr << 1;  // 扩容阈值
            }
        } else if (oldThr > 0) {  // 数组未初始化
            newCap = oldThr;
        } else {  // 初始化时未定义数组大小，使用默认值
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);  // 默认阈值 为 0.75倍
        }
        if (newThr == 0) {  // map 初始化时定义了数组大小
            float ft = loadFactor * newCap;
            newThr = (ft < MAXIMUM_CAPACITY && newCap < MAXIMUM_CAPACITY) ? (int)ft : Integer.MAX_VALUE;
        }
        threshold = newThr;
        Node[] newTab = new Node[newCap];
        tables = newTab;
        if (oldTab != null) {
            for (int i = 0;i < oldCap;i++) {
                Node<K,V> e;
                if ((e = oldTab[i]) != null) {
                    oldTab[i] = null;
                    if (e.next == null) {  // 链表 为null，则可以直接重新赋值
                        newTab[e.hash & (newCap - 1)] = e;
                    } else {
                         // 扩容两倍后 ， cap 值 高一为 变为1  即 原来大小 100（4）  则 1000（8）
                        // 所以原来索引计算为 hash & 011,现在 为 hash & 111
                        // 得出 索引变化 有两种情况  ：
                        // 第一种 高一位 hash为0 则 0 & 1 = 0 索引不变，
                        // 第二种 高一位 & 1 = 1，索引变为 原索引 + oldCap
                        // 即 数组 索引 i 下 链表 每一个元素  新的存储位置 可以重新计算 hash & oldCap + i 得出
                        Node<K,V> loHead = null, loTail = null;  // 原索引位置不变的链表  // head 为 新的链表头部  tail 为 链表尾部
                        Node<K,V> hiHead = null, hiTail = null;  // 索引需要 + cap 的 链表
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {  // 索引位置不变的元素
                                if (loTail == null) {  // 第一个索引不变的元素 放在 链表头部
                                    loHead = e;
                                } else {  // 后续添加的链表  向尾部next 中添加
                                    loTail.next = e;
                                }
                                loTail = e;  // 链表尾部更改
                            } else  {
                                if (hiTail == null) {
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        }while ((e = next) != null);

                        if (loTail != null) {
                            loTail.next = null;
                            newTab[i] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[i + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    private Node<K,V> newNode(K k,V v,int hash,Node<K,V> next) {
        return new Node<>(hash,k,v,next);
    }
}
