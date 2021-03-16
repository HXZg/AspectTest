package com.xz.skin;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {


        /**
         * 设计模式：
         * 创建型模式  单例 构建者 原型 工厂 抽象工厂
         * 结构型模式  适配器 代理 享元 组合 外观 装饰器 桥接
         * 行为型模式  观察者 责任链 备忘录 解释器 命令 模板方法 中介
         *
         * 迪米特法则 两个类不直接互相依赖，解耦
         * 单一职责  一个类只负责一件事
         * 接口隔离  接口设计不宜过于复杂，功能过于庞大
         * 依赖倒置  高层不依赖于底层， 面向接口编程
         * 开闭原则  扩展开放 修改关闭
         * 里氏替换  父类引用指向子类的实例
         * 组合应用  用组合代替继承
         *
         * 类之间解耦的方法
         * 1. 相互之间用接口回调的方式
         * 2. 事件总线 发送订阅事件 EventBus LiveData
         * 3. Hilt Darge2 IOC方式 依赖注入 控制反转
         *
         *
         */

        return "";
    }
}
