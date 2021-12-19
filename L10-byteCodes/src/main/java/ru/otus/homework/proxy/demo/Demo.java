package ru.otus.homework.proxy.demo;

import ru.otus.homework.proxy.LogProxyCreator;

public class Demo {

    public static void main(String[] args) {
        MyClassInterface proxy = LogProxyCreator.getLogProxy(MyClassInterface.class, new MyClassImpl());
        proxy.function1("function1");
        proxy.function2("function2");
        proxy.function3(1, 2);
        proxy.function4(1);
        proxy.function5(2);
    }
}
