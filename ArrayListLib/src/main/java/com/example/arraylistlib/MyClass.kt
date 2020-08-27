package com.example.arraylistlib

import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min

class MyClass {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            /*val animal = Animal("dog", 10)
            val animal1 = animal.clone() as Animal
            animal1.name = "cat"
            println("args = [${animal}] ,,,,, $animal1")

            val list = ArrayList<String>()
            list.removeAll(ArrayList<String>())*/
            MyClass().shellSort()
        }
    }

    // 冒泡排序
    fun maopao() {
        val array = arrayOf(1,3,8,6,4,5)

        var i = array.size - 1
        while(i > 0) {
            var sorted = true  // 针对 已经是有序的数据，则只需一次 ，无需继续循环
            var changeIndex = 0  // 针对 再后面的一系列有序数据  , 则下一次排序 只排序再之前的位置
            for (j in 0 until i) {
                if (array[j] > array[j + 1]) {
                    array[j] = array[j] + array[j + 1]
                    array[j + 1] = array[j] - array[j + 1]
                    array[j] = array[j] - array[j + 1]
                    sorted = false
                    changeIndex = j + 1
                }
            }
            if (sorted) {
                break
            }
            i = changeIndex
            i--
        }
    }

    // 选择排序
    fun selectionOrder() {
        val array = arrayOf(1,3,8,6,4,5)

        for (i in array.size - 1 until 0) {
            var maxIndex = 0
            for (j in 0 until i) {
                if (array[maxIndex] < array[j + 1]) {
                    maxIndex = j + 1
                }
            }
            array[i] = array[i] xor array[maxIndex]  // 异或
            array[maxIndex] = array[i] xor array[maxIndex]
            array[i] = array[i] xor array[maxIndex]
        }

    }

    // 插入排序
    fun  charu() {
        val array = arrayOf(1,3,8,6,4,5)

        var cur = 0
        for (i in 0 until array.size - 1) {
            cur = array[i + 1]  // 记录 当前  需要插入的数据  第i个数据之前已是有序的
            var pre = i
            while (pre >= 0 && cur < array[pre]){
                array[pre + 1] = array[pre]  // 记录的数据较小  第pre个数据 需要往后移一位
                pre--
            }
            array[pre+1] = cur  // 讲 记录的数据插入进去
        }
    }

    // 希尔 排序
    fun shellSort() {
        val array = arrayOf(1,3,8,6,4,5)
        // 定义一个 步长序列  可以随便定义  希尔规定的是 size / 2   则 讲数组 变为一个 size/step * step 的一个矩阵
        val list = arrayListOf<Int>()
        var step = array.size / 2
        while (step > 0) {
            list.add(step)
            step /= 2
        }

        /**
         * 矩阵 index 举例：  step = 2
         * 0 1
         * 2 3
         * 4 5
         * 6 7
         * 排序顺序 步骤1：2 / 0 ， 3 / 1  这里 其实每一个步骤 有两次循环 即 i = 2 ， i = 3
         * 步骤2：4 / 2 / 0 ， 5 / 3 / 1
         * 步骤3：6 / 4 / 2 / 0 ， 7 / 5 / 3 / 1
         */
        list.forEach {
            var cur = 0
            for (i in it until array.size) {  // 矩阵交换排序  第一列矩阵的前两个数排序，再第二列，再 第一列 前三个数排序  i = 2 ，3
                cur = array[i]  // 插入排序的步骤
                var j = i - it
                while (j >= 0) {
                    if (cur < array[j]) {
                        array[j + it] = array[j]
                    } else {  // 如果 index 4 > 2  则 无需继续比较 应为 2 与 0 已经排过序了
                        break
                    }
                    j -= it
                }
                array[j + it] = cur
            }
        }

        array.forEach {
            print("  $it")
        }
    }

    /**
     * 桶排序
     */
    fun tongSort() {
        val array = arrayOf(1,3,8,6,4,5)

        var max = Int.MIN_VALUE
        var min = Int.MAX_VALUE

        // 第一步  取出最大最小值
        array.forEach {
            max = max(it,max)
            min = min(it,min)
        }

        val help = IntArray(max - min){0}
        array.forEach {
            // 第二步  值偏移最小值（防止负数） 作为索引  值为存在的次数
            help[it - min]++
        }

        // 第三步 还原数据
        var j = 0
        help.forEachIndexed { index, i ->
            var count = i
            while (count-- > 0 ){  // 当前索引index  有count个
                // 偏移 min
                array[j++] = index + min
            }
        }
    }


    /**
     * 归并排序
     */
    open fun mergeSort(nums: IntArray): Unit {
        val tmpArray = IntArray(nums.size)
        mSort(nums, tmpArray, 0, nums.size - 1)
    }

    fun mSort(
        nums: IntArray,
        tmps: IntArray,
        left: Int,
        right: Int
    ) {
        val center: Int
        if (left < right) {
            center = (left + right) / 2
            mSort(nums, tmps, left, center)
            mSort(nums, tmps, center + 1, right)
            merge(nums, tmps, left, center + 1, right)
        }
    }

    // 空间归并
    fun merge(
        nums: IntArray,
        tmps: IntArray,
        lpos: Int,
        rpos: Int,
        rightEnd: Int
    ) {
        var lpos = lpos
        var rpos = rpos
        var rightEnd = rightEnd
        var i: Int
        val leftEnd: Int
        val numElements: Int
        var tmpPos: Int
        leftEnd = rpos - 1
        tmpPos = lpos
        numElements = rightEnd - lpos + 1
        while (lpos <= leftEnd && rpos <= rightEnd) {
            if (nums[lpos] <= nums[rpos]) tmps[tmpPos++] =
                nums[lpos++] else tmps[tmpPos++] = nums[rpos++]
        }
        while (lpos <= leftEnd) tmps[tmpPos++] = nums[lpos++]
        while (rpos <= rightEnd) tmps[tmpPos++] = nums[rpos++]
        i = 0
        while (i < numElements) {
            nums[rightEnd] = tmps[rightEnd]
            i++
            rightEnd--
        }
    }

    /**
     * 文件搜索 广度搜索优先  遍历操作     避免递归影响的栈溢出
     */
    fun guangduSearch(parentDir: String,fileName: String) {  // 信息存在堆区 占用内存稳定    递归 每次方法都会入栈，导致栈溢出
        val linkedList = LinkedList<File>()  // 队列  先进先出

        File(parentDir).listFiles()?.forEach {
            if (it.isDirectory) {
                linkedList.offer(it)
            } else {
                if (it.absolutePath.contains(fileName)) {
                    // 找到对应文件 处理
                }
            }
        }

        while (!linkedList.isEmpty()) {
            val file = linkedList.poll()
            val listFiles = file.listFiles() ?: continue
            listFiles.forEach {
                if (it.isDirectory) {
                    linkedList.offer(it)
                } else {
                    if (it.absolutePath.contains(fileName)) {
                        // 找到对应文件 处理
                    }
                }
            }
        }

    }

}