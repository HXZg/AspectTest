package com.example.arraylistlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
//        List<? extends Fruit> list = new ArrayList<>(); // 协变  不能确定是哪个子类所以不能用作参数  规定了泛型的上界  OUT

//        list.add(new Fruit());
//        Fruit s = list.get(0);

//        List<? super Fruit> ll = new ArrayList<>();

//        ll.add(new Fruit());  //

//        Fruit s1 = ll.get(0);   // 逆变 规定了泛型的下界  无法确定具体为哪个超类，所以不允许作为返回值  IN
    }

    static class Fruit {}

    static class Orange extends Fruit {}

    public static long count = 0;

    public static void outChar(char start,char end,String sb) {
        if (start > end) {
            System.out.print(sb + ",,");
            ++count;
            return;
        }
        outChar((char) (start + 1),end,sb + start);
        outChar((char) (start + 1),end,sb);
    }
}
