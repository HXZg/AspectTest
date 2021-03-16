package com.example.ioclib.annotation

/**
 * @title com.example.ioclib.annotation  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des TestAny
 * @DATE 2020/8/6  14:14 星期四
 */

class TestAny : Cloneable{


    fun test() {
        var t = ""
        val list = ArrayList<String>()
        list.clone()
        Any::class.java
    }

    override fun clone(): Any {
        return super.clone()
    }
}