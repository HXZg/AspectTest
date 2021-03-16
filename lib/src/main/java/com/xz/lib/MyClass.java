package com.xz.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class MyClass {

    private static Object[] queue = new Object[15];

    public static void main(String[] args) {
        queue[0] = -1;
        queue[1] = 16;
        queue[2] = 18;
        queue[3] = 46;
        queue[4] = 40;
        queue[5] = 20;
        queue[6] = 60;
        queue[7] = 49;
        queue[8] = 48;
        queue[9] = 50;
//        queue[2] = 10;
//        queue[2] = 0;

        siftUpComparable(11, 7);

        printQueue();

        HashMap map;

        // (size - 1) / 2  (index * 2) + 1  size / 2

        /*PriorityQueue queue1 = new PriorityQueue();
        queue1.add(2);
        queue1.add(0);
        queue1.add(1);
        System.out.println("args = " + queue1.size());
        for (int i = 0; i < queue1.size(); i++) {
            System.out.println("args = " + i + "::" + queue1.poll());
        }*/
    }

    private static void siftUpComparable(int var1, Object var2) {
        Comparable var3;
        int var4;
        for(var3 = (Comparable)var2; var1 > 0; var1 = var4) {
            var4 = var1 - 1 >>> 1;
            Object var5 = queue[var4];
            if (var3.compareTo(var5) >= 0) {
                break;
            }

            queue[var1] = var5;
        }

        queue[var1] = var3;
    }

    private static void printQueue() {
        for (int i = 0; i < queue.length; i++) {
            System.out.println("index::" + i + "::" + queue[i]);
        }
    }
}
